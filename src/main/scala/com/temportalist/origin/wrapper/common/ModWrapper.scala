package com.temportalist.origin.wrapper.common

import java.util

import com.temportalist.origin.api.IProxy
import com.temportalist.origin.library.common.handlers.{RegisterHelper, OptionHandler}
import com.temportalist.origin.library.common.register._
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
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
			event: FMLPreInitializationEvent, proxy: IProxy,
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
			sortedRegArray.get(index).asInstanceOf[EntityRegister].addEntitySpawns
		}

		// TODO enchantments

		// TODO achievement

		RegisterHelper.registerHandler(this, proxy)

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy)

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

	protected def initialize(event: FMLInitializationEvent, proxy: IProxy): Unit = {
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

	protected def postInitialize(event: FMLPostInitializationEvent, proxy: IProxy): Unit = {
		proxy.postInit()
	}

}
