package com.temportalist.origin.internal.common

import java.util
import java.util.UUID

import com.temportalist.origin.api.common.IModDetails
import com.temportalist.origin.foundation.common.IMod
import com.temportalist.origin.foundation.common.item.ItemPlacer
import com.temportalist.origin.foundation.common.network._
import com.temportalist.origin.foundation.common.utility.Players
import com.temportalist.origin.internal.common.extended.ExtendedSync
import com.temportalist.origin.internal.common.handlers.{OptionHandler, RegisterHelper}
import com.temportalist.origin.internal.common.item.ItemEgg
import com.temportalist.origin.internal.server.command.CommandOrigin
import com.temportalist.origin.test.{PacketUpdateMode, Sonic}
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util._
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.event.ServerChatEvent

/**
 *
 *
 * @author TheTemportalist
 */
@Mod(modid = Origin.MODID, name = Origin.MODNAME, version = Origin.VERSION,
	guiFactory = Origin.clientProxy,
	modLanguage = "scala"
)
object Origin extends IMod with IModDetails {

	final val MODID = "origin"
	final val MODNAME = "Origin"
	final val VERSION = "4.0"
	//todo"@PLUGIN_VERSION@"
	final val clientProxy = "com.temportalist.origin.library.client.ProxyClient"
	final val serverProxy = "com.temportalist.origin.library.server.ProxyServer"

	override def getModid: String = this.MODID

	override def getModName: String = this.MODNAME

	override def getModVersion: String = this.VERSION

	override def getDetails: IModDetails = this

	@SidedProxy(clientSide = this.clientProxy, serverSide = this.serverProxy)
	var proxy: ProxyCommon = null

	var dimensions: util.HashMap[String, Int] = new util.HashMap[String, Int]
	var dimensions1: util.HashMap[Int, String] = new util.HashMap[Int, String]

	private val tabItems: util.ArrayList[Item] = new util.ArrayList[Item]
	private val tabBlocks: util.ArrayList[Block] = new util.ArrayList[Block]

	def addItemToTab(item: Item): Unit = {
		tabItems.add(item)
	}

	def addBlockToTab(block: Block): Unit = {
		tabBlocks.add(block)
	}

	var placer: ItemPlacer = null

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent): Unit = {
		RegisterHelper.registerHandler(ExtendedSync, OptionHandler, Players)
		super.preInitialize(this.MODID, this.MODNAME, event, this.proxy, CGOOptions)

		RegisterHelper.registerCommand(CommandOrigin)

		this.registerPackets(
			classOf[PacketSyncExtendedProperties],
			classOf[PacketTeleport],
			classOf[PacketRedstoneUpdate],
			classOf[PacketActionUpdate], classOf[PacketUpdateMode],
			classOf[PacketTileCallback]
		)

		this.placer = new ItemEgg(Origin.MODID, "placer")
		// todo Sonic.preInit(event.getModConfigurationDirectory)

	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent): Unit = {
		super.initialize(event, this.proxy)
		println(Sonic.entityStates)
	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent): Unit = {
		super.postInitialize(event, this.proxy)

		if (!this.tabItems.isEmpty || !this.tabBlocks.isEmpty) {
			val originTab: CreativeTabs = new CreativeTabs(Origin.MODID) {
				override def getTabIconItem: Item = {
					Items.carrot_on_a_stick
				}
			}

			for (index <- 0 until this.tabBlocks.size()) {
				this.tabBlocks.get(index).setCreativeTab(originTab)
			}
			for (index <- 0 until this.tabItems.size()) {
				this.tabItems.get(index).setCreativeTab(originTab)
			}

		}

		// todo Sonic.postInit()

	}

	@Mod.EventHandler
	def serverLoad(event: FMLServerStartingEvent): Unit = {

		for (command <- RegisterHelper.getCommands()) {
			event.registerServerCommand(command)
		}

		val allWS: Array[WorldServer] = DimensionManager.getWorlds
		val temp: util.HashMap[String, Integer] = new util.HashMap[String, Integer]

		for (i <- 0 until allWS.length) {
			temp.put(allWS(i).provider.getDimensionName, allWS(i).provider.dimensionId)
		}

		Origin.dimensions.clear
		Origin.dimensions1.clear
		val keys: util.SortedSet[String] = new util.TreeSet[String](temp.keySet)
		val iterator: util.Iterator[String] = keys.iterator()
		while (iterator.hasNext) {
			val key: String = iterator.next()
			val id: Int = temp.get(key)
			Origin.dimensions.put(key, id)
			Origin.dimensions1.put(id, key)
		}

	}

	private final val temportalist: UUID = UUID.fromString("dcb7f6a8-9f0d-4d6d-81f0-356e7b05f78f")
	private final val progwml6: UUID = UUID.fromString("83898b28-6118-4900-9137-41ffc46b6e10")

	/**
	 * SHHHHHHHH! SECRET PUMPKIN!
	 * @param event
	 */
	@SubscribeEvent
	def onPlayerJoin(event: PlayerLoggedInEvent): Unit = {
		if (event.player.getGameProfile.getId.equals(this.progwml6)) {
			if (event.player.getCurrentArmor(3) == null) {
				val secretPumpkin: ItemStack = new ItemStack(Blocks.pumpkin, 1, 0)
				secretPumpkin.addEnchantment(Enchantment.unbreaking, 5)
				secretPumpkin.setStackDisplayName("Pumpkin of Awesomeness")
				event.player.setCurrentItemOrArmor(4, secretPumpkin)
			}
		}
	}

	@SubscribeEvent
	def serverChat(event: ServerChatEvent): Unit = {
		if (event.player.getGameProfile.getId.equals(this.temportalist)) {
			var color: EnumChatFormatting = null
			while (color == null) {
				color = EnumChatFormatting.values()(
					event.player.worldObj.rand.nextInt(EnumChatFormatting.values().length)
				)
				if (!color.isColor) color = null
			}
			event.component.setChatStyle(new ChatStyle().setColor(color))
		}
	}

}
