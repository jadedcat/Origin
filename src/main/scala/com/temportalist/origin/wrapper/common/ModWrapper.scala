package com.temportalist.origin.wrapper.common

import java.util

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.helpers.OptionHandler
import com.temportalist.origin.library.common.lib.LogHelper
import com.temportalist.origin.library.common.register._
import net.minecraftforge.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry

/**
 *
 *
 * @author TheTemportalist
 */
class ModWrapper() {

	var options: OptionRegister = null

	private var sortedRegisters: util.HashMap[String, util.ArrayList[Register]] = null

	protected def preInitialize(modid: String, modname: String,
			event: FMLPreInitializationEvent, proxy: ProxyWrapper,
			registers: Register*): Unit = {

		this.sortedRegisters = new util.HashMap[String, util.ArrayList[Register]]()
		this.organizeRegisters(sortedRegisters, registers)

		var sortedRegArray: util.ArrayList[Register] = null

		sortedRegArray = sortedRegisters.get("option")
		for (index <- 0 until sortedRegArray.size()) {
			OptionHandler.handleConfiguration(modid, modname,
				sortedRegArray.get(index).asInstanceOf[OptionRegister], event)
		}

		sortedRegArray = sortedRegisters.get("item")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).register()
		}

		sortedRegArray = sortedRegisters.get("block")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerTileEntities
			sortedRegArray.get(index).register()
		}

		sortedRegArray = sortedRegisters.get("item")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerItemsPostBlock
		}

		// TODO biomes

		sortedRegArray = sortedRegisters.get("entity")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).register()
			sortedRegArray.get(index).asInstanceOf[EntityRegister].addEntityMappings
			sortedRegArray.get(index).asInstanceOf[EntityRegister].addEntitySpawns
		}

		// TODO enchantments

		// TODO achievement

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy)

		// Packet Registration
		//todo does not find any IPackets
		val packageName: String = this.getClass.getPackage.getName
		LogHelper.info(Origin.pluginName,
			"Looking for packets to register for channel \'" + modid + "\' in package \'" +
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

	protected def initialize(event: FMLInitializationEvent, proxy: ProxyWrapper): Unit = {
		var sortedRegArray: util.ArrayList[Register] = null

		sortedRegArray = sortedRegisters.get("item")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerCrafting
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerSmelting
			sortedRegArray.get(index).asInstanceOf[ItemRegister].registerOther
		}

		sortedRegArray = sortedRegisters.get("block")
		for (index <- 0 until sortedRegArray.size()) {
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerCrafting
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerSmelting
			sortedRegArray.get(index).asInstanceOf[BlockRegister].registerOther
		}

		proxy.registerRender()

	}

	protected def postInitialize(event: FMLPostInitializationEvent): Unit = {

	}

}
