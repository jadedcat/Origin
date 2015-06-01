package com.temportalist.origin.test

import java.io._
import java.net.{URL, URLConnection}
import java.util.{Random, UUID}

import com.google.gson.{JsonArray, JsonElement, JsonObject}
import com.temportalist.origin.api.common.register.Registry
import com.temportalist.origin.api.common.utility.{Json, NBTHelper, Scala, WorldHelper}
import com.temportalist.origin.internal.common.Origin
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.event.world.WorldEvent

import scala.collection.mutable

/**
 *
 *
 * @author TheTemportalist
 */
object Sonic {

	var screwdriver: ItemScrewdriver = null
	val screwdrivers: mutable.ListBuffer[ItemStack] = mutable.ListBuffer[ItemStack]()

	val entityStates: mutable.Map[String, EntityState] = mutable.Map()
	val random: Random = new Random()
	var currentCommitSha: String = null

	def preInit(configDir: File): Unit = {

		Registry.registerHandler(this)

		this.screwdriver = new ItemScrewdriver("screwdriver")
		Origin.addItemToTab(this.screwdriver)

		for (i <- 0 to 2) {
			val stack: ItemStack = new ItemStack(this.screwdriver, 1, i)
			ScrewdriverMode.initModes(stack)
			this.screwdrivers += stack
		}

		val statesDir: File = new File(configDir, "Origin-DataCoreStates")
		if (!statesDir.exists()) statesDir.mkdir()
		else Scala.foreach(statesDir.list(), (index: Int, modFileName: String) => {
			val modDirFile: File = new File(statesDir, modFileName)
			Scala.foreach(modDirFile.list(), (index: Int, fileName: String) => {
				val entityString = fileName.split('.')(0)
				if (!Sonic.entityStates.contains(entityString)) {
					val json: JsonObject = Json.getJson(
						new File(modDirFile, fileName)).getAsJsonObject

					val state: EntityState = new EntityState
					state.owner = modFileName
					state.entityString = entityString
					state.name = json.get("name").getAsString
					val nbt: NBTTagCompound = Json.jsonToNBT(
						json.get("nbt").getAsJsonObject).asInstanceOf[NBTTagCompound]

					overwriteWithDefaultEntityNBT(nbt)

					state.setEntityNBT(nbt)

					state.register()
				}
			})
		})

	}

	def postInit(): Unit = {
		this.fetchRepoedEntityFiles()
	}

	def fetchRepoedEntityFiles(): Unit = {
		new Thread(new Runnable {
			override def run(): Unit = {
				this.fetchCommitSha()
				this.findEntities()
			}

			def fetchCommitSha(): Unit = {
				val url: String = "https://api.github.com/repos/TheTemportalist/Origin/commits"
				val fileIn: Reader = new InputStreamReader(new URL(url).openStream())
				val commitJson: JsonElement = Json.getJson(fileIn)
				fileIn.close()
				Sonic.currentCommitSha = commitJson.getAsJsonArray.get(0).
						getAsJsonObject.get("sha").getAsString
			}

			def findEntities(): Unit = {
				val fullPathPrefix: String = "https://raw.githubusercontent.com/TheTemportalist/Origin-DataCore-Resources/master/"
				val statesURL: String = "https://api.github.com/repos/TheTemportalist/Origin-DataCore-Resources/git/trees/" // todo fix this
				this.doTreeIterationFromGitContents(statesURL,
					(index: Int, element: JsonElement) => {
						val folderName: String = element.getAsJsonObject.get("path").getAsString
						if (folderName.equals("vanilla") ||
								FMLCommonHandler.instance().findContainerFor(folderName) != null) {
							val folderURL: String = element.getAsJsonObject.get("url").getAsString
							this.doTreeIterationFromGitContents(folderURL,
								(index: Int, element: JsonElement) => {
									val path: String = element.getAsJsonObject.get("path")
											.getAsString
									val entityString: String = path.split('.')(0)
									if (!Sonic.entityStates.contains(entityString)) {
										val state: EntityState = this.getStateFromURL(
											fullPathPrefix + folderName + "/" + path
										)
										state.owner = folderName
										state.entityString = entityString
										state.register()
									}
								}
							)
						}
					}
				)
			}

			def doTreeIterationFromGitContents(url: String,
					callback: (Int, JsonElement) => Unit): Unit = {
				val fileIn: Reader = new InputStreamReader(new URL(url).openStream())
				val folderElement: JsonElement = Json.parser.parse(fileIn)
				fileIn.close()
				val folderList: JsonArray = folderElement.getAsJsonObject.getAsJsonArray("tree")
				Scala.foreach(folderList, callback)
			}

			def getStateFromURL(url: String): EntityState = {
				val fileIn: Reader = new InputStreamReader(new URL(url).openStream())
				val state: EntityState = Json.gson.fromJson(fileIn, classOf[EntityState])
				fileIn.close()
				state
			}

		}).start()
	}

	def overwriteWithDefaultEntityNBT(nbt: NBTTagCompound): Unit = {
		val id: UUID = UUID.randomUUID()
		nbt.setLong("UUIDMost", id.getMostSignificantBits)
		nbt.setLong("UUIDLeast", id.getLeastSignificantBits)

		nbt.setInteger("Dimension", 0)
		nbt.setTag("Pos", NBTHelper.getNBT(0D, 0D, 0D))
		nbt.setTag("Rotation", NBTHelper.getNBT(0F, 0F))
		nbt.setTag("Motion", NBTHelper.getNBT(0D, 0D, 0D))
	}

	@SubscribeEvent
	def worldLoad(event: WorldEvent.Load): Unit = {
		if (WorldHelper.isOverworld(event.world)) {
			for (state: EntityState <- this.entityStates.values) {
				if (!state.hasCreatedEntity) state.createEntity(event.world)
			}
		}
	}

	def researchEntity(eID: String, entity: Entity, id: UUID): Unit = {
		println("Researching " + entity.getCommandSenderName)
		val state: EntityState = new EntityState()

		state.owner = "Unknown"
		state.entityString = eID
		state.name = entity.getCommandSenderName

		val entityNBT: NBTTagCompound = new NBTTagCompound
		this.overwriteWithDefaultEntityNBT(entityNBT)
		state.setEntityNBT(entityNBT)

		state.setEntity(entity)

		this.entityStates(eID) = state

		this.postEntityStateForSaving(id, state)

	}

	def postEntityStateForSaving(id: UUID, state: EntityState): Unit = {
		val uuid: String = id.toString
		val modid: String = "Unknown"
		val entityID: String = state.name
		val className: String = state.getEntity.getClass.getCanonicalName

		val jsonNBT: JsonObject = Json.nbtToJson(state.getEntityNBT).getAsJsonObject
		jsonNBT.addProperty("entityClass", className)
		val data: String = "uuid=" + uuid + "&modid=" + modid + "&file=" + entityID + "&data=" +
			Json.toReadableString(jsonNBT.toString)

		new Thread(new Runnable {
			override def run(): Unit = {
				try {
					val url: URL = new URL("http://thetemportalist.dries007.net/datacore.php")
					val conn: URLConnection = url.openConnection()

					conn.setDoOutput(true)

					val writer: OutputStreamWriter = new OutputStreamWriter(conn.getOutputStream)

					writer.write(data)
					writer.flush()
					writer.close()

					val reader: BufferedReader = new BufferedReader(
						new InputStreamReader(conn.getInputStream))
					var line: String = reader.readLine
					while (line != null) {
						println(line)
						line = reader.readLine()
					}

					reader.close()

				} catch {
					case e: Exception =>
						e.printStackTrace();
				}
			}
		}).start()

	}

}
