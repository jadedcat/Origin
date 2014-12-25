package com.temportalist.origin.library.common.helpers

import java.io.File
import java.util

import com.temportalist.origin.library.common.register.OptionRegister
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 *
 *
 * @author TheTemportalist
 */
object OptionHandler {

	private val handlers: util.HashMap[String, OptionRegister] = new
					util.HashMap[String, OptionRegister]()

	def handleConfiguration(pluginID: String, pluginName: String, options: OptionRegister,
			event: FMLPreInitializationEvent): Unit = {
		if (options.hasCustomConfiguration()) {
			options.customizeConfiguration(event)
		}

		if (options.hasDefaultConfig() && options.config == null) {
			val cfgFile: File = new
							File(options.getConfigDirectory(event.getModConfigurationDirectory),
								pluginName + ".cfg")
			options.config = new Configuration(cfgFile, true)
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
