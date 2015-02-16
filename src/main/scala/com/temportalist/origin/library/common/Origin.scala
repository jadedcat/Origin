package com.temportalist.origin.library.common

import java.util

import com.temportalist.origin.library.common.extended.ExtendedSync
import com.temportalist.origin.library.common.handlers.{OptionHandler, RegisterHelper}
import com.temportalist.origin.library.common.network._
import com.temportalist.origin.library.server.command.TeleportCommand
import com.temportalist.origin.wrapper.common.ModWrapper
import com.temportalist.origin.wrapper.common.item.ItemPlacer
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util._
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.event._
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.{Mod, SidedProxy}

/**
 *
 *
 * @author TheTemportalist
 */
@Mod(modid = Origin.MODID, name = Origin.MODNAME, version = Origin.VERSION,
	guiFactory = Origin.clientProxy,
	modLanguage = "scala"
)
object Origin extends ModWrapper {

	final val MODID = "origin"
	final val MODNAME = "Origin"
	final val VERSION = "4.0"
	//todo"@PLUGIN_VERSION@"
	final val clientProxy = "com.temportalist.origin.library.client.ClientProxy"
	final val serverProxy = "com.temportalist.origin.library.server.ServerProxy"

	@SidedProxy(clientSide = this.clientProxy, serverSide = this.serverProxy)
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

	var placer: ItemPlacer = null

	@Mod.EventHandler
	def preInit(event: FMLPreInitializationEvent): Unit = {
		RegisterHelper.registerHandler(ExtendedSync, OptionHandler)
		super.preInitialize(this.MODID, this.MODNAME, event, this.proxy, CGOOptions)

		RegisterHelper.registerCommand(TeleportCommand)

		RegisterHelper.registerPacketHandler(this.MODID,
			classOf[PacketSyncExtendedProperties],
			classOf[PacketTeleport],
			classOf[PacketRedstoneUpdate],
			classOf[PacketActionUpdate]
		)

		this.placer = new ItemPlacer(Origin.MODID, "placer")

	}

	@Mod.EventHandler
	def init(event: FMLInitializationEvent): Unit = {
		super.initialize(event, this.proxy)
	}

	@Mod.EventHandler
	def postInit(event: FMLPostInitializationEvent): Unit = {
		super.postInitialize(event)

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

	}

	@Mod.EventHandler
	def serverLoad(event: FMLServerStartingEvent): Unit = {

		for (command <- RegisterHelper.getCommands()) {
			event.registerServerCommand(command)
		}

		val allWS: Array[WorldServer] = DimensionManager.getWorlds
		val temp: util.HashMap[String, Integer] = new util.HashMap[String, Integer]

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

	///*
	@SubscribeEvent
	def serverChat(event: ServerChatEvent): Unit = {
		if (event.username.equals("TheTemportalist")) {
			// todo use uuid
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

	//*/

	/*
	@SubscribeEvent
	def onChat(event: ClientChatReceivedEvent): Unit = {
		val parent: IChatComponent = event.message
		println(event.message.getUnformattedText)
		println(event.message.getUnformattedTextForChat)
		println(event.message.getFormattedText)
	}
	*/

}
