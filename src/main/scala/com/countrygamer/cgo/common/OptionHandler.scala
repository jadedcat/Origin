package com.countrygamer.cgo.common

import java.io.File
import java.util

import com.countrygamer.cgo.wrapper.common.registries.OptionRegister
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.config.Configuration

/**
 *
 *
 * @author CountryGamer
 */
object OptionHandler {

	private val handlers: util.HashMap[String, OptionRegister] = new
					util.HashMap[String, OptionRegister]()

	def handleConfiguration(pluginID: String, pluginName: String, options: OptionRegister,
			event: FMLPreInitializationEvent): Unit = {
		if (options.hasCustomConfiguration) {
			options.customizeConfiguration(event)
		}
		else {
			if (options.config == null) {
				val cfgFile: File = new
								File(event.getModConfigurationDirectory, pluginName + ".cfg")
				options.config = new Configuration(cfgFile, true)
			}
		}
		options.loadConfiguration()
		this.handlers.put(pluginID, options)
	}

	@SubscribeEvent
	def onConfigurationChanged(event: OnConfigChangedEvent): Unit = {
		val iterator: util.Iterator[String] = this.handlers.keySet().iterator()
		while (iterator.hasNext) {
			val pluginID: String = iterator.next()
			if (event.modID.equalsIgnoreCase(pluginID)) {
				this.handlers.get(pluginID).loadConfiguration()
			}
		}
	}

}
