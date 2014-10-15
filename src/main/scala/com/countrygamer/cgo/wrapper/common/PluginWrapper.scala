package com.countrygamer.cgo.wrapper.common

import java.util

<<<<<<< HEAD
import com.countrygamer.cgo.library.common.helpers.OptionHandler
=======
import com.countrygamer.cgo.library.common.Origin
import com.countrygamer.cgo.library.common.helpers.OptionHandler
import com.countrygamer.cgo.library.common.lib.LogHelper
>>>>>>> 4614287... Much updates. Many things have been moved and re-organized.
import com.countrygamer.cgo.library.common.register._
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry

/**
 *
 *
 * @author CountryGamer
 */
class PluginWrapper() {

	var options: OptionRegister = null

	protected def preInitialize(pluginID: String, pluginName: String,
			event: FMLPreInitializationEvent, proxy: ProxyWrapper,
			registers: Register*): Unit = {

		val sortedRegisters: util.HashMap[String, util.ArrayList[Register]] = new
						util.HashMap[String, util.ArrayList[Register]]()
		this.organizeRegisters(sortedRegisters, registers)

		var sortedRegArray: util.ArrayList[Register] = null
		var index: Int = 0

		sortedRegArray = sortedRegisters.get("option")
		for (index <- 0 until sortedRegArray.size()) {
			OptionHandler.handleConfiguration(pluginID, pluginName,
				sortedRegArray.get(index).asInstanceOf[OptionRegister], event)
		}

		index = 0
		sortedRegArray = sortedRegisters.get("item")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).register()
		}

		index = 0
		sortedRegArray = sortedRegisters.get("block")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerTileEntities
			sortedRegArray.get(index).register()
		}

		index = 0
		sortedRegArray = sortedRegisters.get("item")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerItemsPostBlock
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerCrafting
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerSmelting
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerOther
		}

		index = 0
		sortedRegArray = sortedRegisters.get("block")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerCrafting
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerSmelting
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerOther
		}

		// TODO biomes

		index = 0
		sortedRegArray = sortedRegisters.get("entity")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).register()
			sortedRegArray.get(index).asInstanceOf[EntityRegister].addEntityMappings
			sortedRegArray.get(index).asInstanceOf[EntityRegister].addEntitySpawns
		}

		// TODO enchantments

		// TODO achievement

		proxy.registerRender()
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy)

		// Packet Registration
		//todo does not find any IPackets
		val packageName: String = this.getClass.getPackage.getName
		LogHelper.info(Origin.pluginName,
			"Looking for packets to register for channel \'" + pluginID + "\' in package \'" +
					packageName + "\'."
		)
		try {
			/*
			val fullurl: String = packageName + classOf[IPacket].getName
			val classLoader: ClassLoader = Thread.currentThread().getContextClassLoader
			// todo does this recursively get resources?
			val resources: Enumeration[URL] = classLoader.getResource(fullurl)



			val packets: Array[Class[_ <: IPacket]] =
				new ResourceFinder(this.getClass.getPackage.getName)
						.findAllImplementations(classOf[IPacket]).toArray()
						.asInstanceOf[Array[Class[_ <: IPacket]]]
			if (packets.length > 0) {
				LogHelper.info(Origin.pluginName,
					"Found " + packets.length + " packets in overall package " +
							packageName + ". Registering to channel \'" + pluginID + "\'."
				)
				PacketHandler.registerHandler(pluginID, packets)
			}
			*/
		}
		catch {
			case e: Exception =>

		}

	}

	private def organizeRegisters(map: util.HashMap[String, util.ArrayList[Register]],
			registers: Seq[Register]): Unit = {

		map.clear()

		map.put("item", new util.ArrayList[Register])
		map.put("block", new util.ArrayList[Register])
		map.put("entity", new util.ArrayList[Register])
		map.put("option", new util.ArrayList[Register])

		var registerIndex: Int = 0
		for (registerIndex <- 0 until registers.length) {
			registers(registerIndex) match {
				case _: ItemRegister =>
					map.get("item").add(registers(registerIndex))
				case register: BlockRegister =>
					map.get("block").add(register)
				case register: EntityRegister =>
					map.get("entity").add(register)
				case register: OptionRegister =>
					map.get("option").add(register)
					this.options = register
				case _ =>
			}
		}

	}

	protected def initialize(event: FMLInitializationEvent): Unit = {

	}

	protected def postInitialize(event: FMLPostInitializationEvent): Unit = {

	}

}
