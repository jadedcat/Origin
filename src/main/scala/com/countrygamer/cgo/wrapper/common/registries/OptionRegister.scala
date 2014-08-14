package com.countrygamer.cgo.wrapper.common.registries

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.common.config.Configuration

/**
 *
 *
 * @author CountryGamer
 */
class OptionRegister() extends Register {

	/**
	 * Stores the configuration data
	 */
	var config: Configuration = null

	/**
	 * Tell the core that this register has a custom configuration to handle
	 * @return false for normal configuration
	 */
	def hasCustomConfiguration(): Boolean = {
		false
	}

	/**
	 * Do custom things to create/use a config file(s).
	 * @see OptionHandler.handleConfiguration
	 * @param event
	 */
	def customizeConfiguration(event: FMLPreInitializationEvent): Unit = {
	}

	def loadConfiguration(): Unit = {
		try {
			this.register()
		}
		catch {
			case e: Exception =>
		}
		finally {
			if (this.config != null && this.config.hasChanged) {
				this.config.save
			}
		}
	}

	def hasDefaultConfig(): Boolean = {
		true
	}

	override def register(): Unit = {}

	@SideOnly(Side.CLIENT)
	def getGuiConfigClass: Class[_ <: net.minecraft.client.gui.GuiScreen] = {
		null
	}

}
