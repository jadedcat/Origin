package com.countrygamer.cgo.wrapper.common

import java.util

import com.countrygamer.cgo.wrapper.common.registries._
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
			if (registers(registerIndex).isInstanceOf[ItemRegister]) {
				map.get("item").add(registers(registerIndex))
			}
			else if (registers(registerIndex).isInstanceOf[BlockRegister]) {
				map.get("block").add(registers(registerIndex).asInstanceOf[BlockRegister])
			}
			else if (registers(registerIndex).isInstanceOf[EntityRegister]) {
				map.get("entity").add(registers(registerIndex).asInstanceOf[EntityRegister])
			}
			else if (registers(registerIndex).isInstanceOf[OptionRegister]) {
				map.get("option").add(registers(registerIndex).asInstanceOf[OptionRegister])
				this.options = registers(registerIndex).asInstanceOf[OptionRegister]
			}
		}

	}

	protected def initialize(event: FMLInitializationEvent): Unit = {

	}

	protected def postInitialize(event: FMLPostInitializationEvent): Unit = {

	}

}
