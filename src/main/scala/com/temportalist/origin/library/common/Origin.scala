package com.temportalist.origin.library.common

import java.util
import java.util.Random

import com.temportalist.origin.library.common.command.TeleportCommand
import com.temportalist.origin.library.common.extended.ExtendedSync
import com.temportalist.origin.library.common.helpers.{OptionHandler, RegisterHelper}
import com.temportalist.origin.library.common.network._
import com.temportalist.origin.wrapper.common.PluginWrapper
import cpw.mods.fml.common._
import cpw.mods.fml.common.event._
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemFood, ItemStack}
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.event.entity.living.LivingDropsEvent

/**
 *
 *
 * @author TheTemportalist
 */
@Mod(modid = Origin.pluginID, name = Origin.pluginName, version = "@PLUGIN_VERSION@",
	guiFactory = Origin.clientProxy,
	modLanguage = "scala",
	dependencies = "required-after:Forge@[10.13,)"
)
object Origin extends PluginWrapper {

	final val pluginID = "origin"
	final val pluginName = "Origin"
	final val clientProxy = "com.temportalist.origin.library.client.ClientProxy"
	final val serverProxy = "com.temportalist.origin.library.server.ServerProxy"

	@SidedProxy(
		clientSide = this.clientProxy,
		serverSide = this.serverProxy
	)
	var proxy: CommonProxy = null

	var dimensions: util.HashMap[String, Int] = new util.HashMap[String, Int]
	var dimensions1: util.HashMap[Int, String] = new util.HashMap[Int, String]

	private val tabItems: util.ArrayList[Item] = new util.ArrayList[Item]
	private val tabBlocks: util.ArrayList[Block] = new util.ArrayList[Block]

	def addItemToTab(item: Item) {
		tabItems.add(item)
	}

	def addBlockToTab(block: Block) {
		tabBlocks.add(block)
	}

	var lambchop_raw: Item = null
	var lambchop: Item = null

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		FMLCommonHandler.instance().bus().register(OptionHandler)
		RegisterHelper.registerHandler(ExtendedSync, null)
		super.preInitialize(this.pluginID, this.pluginName, event, this.proxy, CGOOptions)

		RegisterHelper.registerHandler(this, null)
		RegisterHelper.registerCommand(TeleportCommand)

		RegisterHelper.registerPacketHandler(this.pluginID, classOf[PacketSyncExtendedProperties],
			classOf[PacketTeleport], classOf[PacketRedstoneUpdate], classOf[PacketActionUpdate])
		if (CGOOptions.enableLambchops) {
			Origin.lambchop_raw = new ItemFood(3, 0.3F, true).setUnlocalizedName("lambchop_raw")
					.setTextureName(Origin.pluginID + ":lambchop_raw")
			GameRegistry.registerItem(Origin.lambchop_raw, "lambchop_raw")
			Origin.lambchop = new ItemFood(8, 0.8F, true).setUnlocalizedName("lambchop")
					.setTextureName(Origin.pluginID + ":lambchop")
			GameRegistry.registerItem(Origin.lambchop, "lambchop")
			GameRegistry.addSmelting(Origin.lambchop_raw, new ItemStack(Origin.lambchop), 0.35F)

		}

	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent) {
		super.initialize(event)

	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent) {
		super.postInitialize(event)

		if (!this.tabItems.isEmpty || !this.tabBlocks.isEmpty) {
			val originTab: CreativeTabs = new CreativeTabs(Origin.pluginID) {
				override def getTabIconItem: Item = {
					net.minecraft.init.Items.iron_pickaxe
				}
			}

			var index: Int = 0

			for (index <- 0 until this.tabBlocks.size()) {
				this.tabBlocks.get(index).setCreativeTab(originTab)
			}
			for (index <- 0 until this.tabItems.size()) {
				this.tabItems.get(index).setCreativeTab(originTab)
			}

		}

	}

	@SubscribeEvent
	def livingDrops(event: LivingDropsEvent) {
		if (CGOOptions.enableLambchops && event.entityLiving != null &&
				event.entityLiving.isInstanceOf[EntitySheep]) {
			val random: Random = new Random()

			val stackSize: Int = random.nextInt(3) + 1 + random.nextInt(1 + event.lootingLevel) + 1

			var item: Item = null
			if (event.entityLiving.isBurning)
				item = Origin.lambchop
			else
				item = Origin.lambchop_raw

			var count: Int = 0
			for (count <- 1 to stackSize) {
				event.drops
						.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX,
					event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(item, 1, 0)))
			}
		}

	}

	@Mod.EventHandler
	def serverLoad(event: FMLServerStartingEvent) {

		for (command <- RegisterHelper.getCommands()) {
			event.registerServerCommand(command)
		}

		val allWS: Array[WorldServer] = DimensionManager.getWorlds
		val temp: util.HashMap[String, Integer] = new util.HashMap[String, Integer]

		var i: Int = 0
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

	/**
	 * SHHHHHHHH! SECRET PUMPKIN!
	 * @param event
	 */
	@SubscribeEvent
	def onPlayerJoin(event: PlayerLoggedInEvent) {
		val playerName: String = event.player.getCommandSenderName
		if (playerName.equals("progwml6")) {
			if (event.player.getCurrentArmor(3) == null) {
				val pumpkin: ItemStack = new ItemStack(Blocks.pumpkin, 1, 0)
				pumpkin.setStackDisplayName("Pumpkin of Awesomeness")
				event.player.setCurrentItemOrArmor(4, pumpkin)
			}
		}
	}

}
