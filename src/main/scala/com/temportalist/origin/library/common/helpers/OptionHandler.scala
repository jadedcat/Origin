package com.temportalist.origin.library.common.helpers

import java.io.File
import java.util

import com.temportalist.origin.library.common.lib.ConfigJson
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
		if (options.config == null) {
			val dir: File = options.getConfigDirectory(event.getModConfigurationDirectory)
			var cfgFile: File = null
			options.getExtension() match {
				case "cfg" =>
					cfgFile = new File(dir, pluginName + ".cfg")
					options.config = new Configuration(cfgFile, true)
				case "json" =>
					cfgFile = new File(dir, pluginName + ".json")
					options.config = new ConfigJson(cfgFile)
				case _ =>
					options.customizeConfiguration(event)
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
