package com.temportalist.origin.library.common

import java.util

import com.temportalist.origin.library.common.command.TeleportCommand
import com.temportalist.origin.library.common.extended.ExtendedSync
import com.temportalist.origin.library.common.helpers.{OptionHandler, RegisterHelper}
import com.temportalist.origin.library.common.network._
import com.temportalist.origin.wrapper.common.ModWrapper
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.fml.common.event._
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod, SidedProxy}

/**
 *
 *
 * @author TheTemportalist
 */
@Mod(modid = Origin.pluginID, name = Origin.pluginName, version = "@PLUGIN_VERSION@",
	guiFactory = Origin.clientProxy,
	modLanguage = "scala"
)
object Origin extends ModWrapper {

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

	def addItemToTab(item: Item): Unit = {
		tabItems.add(item)
	}

	def addBlockToTab(block: Block): Unit = {
		tabBlocks.add(block)
	}

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent): Unit = {
		FMLCommonHandler.instance().bus().register(OptionHandler)
		RegisterHelper.registerHandler(ExtendedSync, null)
		super.preInitialize(this.pluginID, this.pluginName, event, this.proxy, CGOOptions)

		RegisterHelper.registerHandler(this, null)
		RegisterHelper.registerCommand(TeleportCommand)

		RegisterHelper.registerPacketHandler(this.pluginID, classOf[PacketSyncExtendedProperties],
			classOf[PacketTeleport], classOf[PacketRedstoneUpdate], classOf[PacketActionUpdate])



	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent): Unit = {
		super.initialize(event)

	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent): Unit = {
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

	@Mod.EventHandler
	def serverLoad(event: FMLServerStartingEvent): Unit = {

		for (command <- RegisterHelper.getCommands()) {
			event.registerServerCommand(command)
		}

		val allWS: Array[WorldServer] = DimensionManager.getWorlds
		val temp: util.HashMap[String, Integer] = new util.HashMap[String, Integer]

		var i: Int = 0
		for (i <- 0 until allWS.length) {
			temp.put(allWS(i).provider.getDimensionName, allWS(i).provider.getDimensionId)
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
	def onPlayerJoin(event: PlayerLoggedInEvent): Unit = {
		val playerName: String = event.player.getName
		if (playerName.equals("progwml6")) {
			if (event.player.getCurrentArmor(3) == null) {
				val secretPumpkin: ItemStack = new ItemStack(Blocks.pumpkin, 1, 0)
				secretPumpkin.addEnchantment(Enchantment.unbreaking, 5)
				secretPumpkin.addEnchantment(Enchantment.lure, 20)
				secretPumpkin.setStackDisplayName("Pumpkin of Awesomeness")
				event.player.setCurrentItemOrArmor(4, secretPumpkin)
			}
		}
	}

}
