package com.countrygamer.cgo.library.common

import com.countrygamer.cgo.library.client.gui.config.GuiConfig
import com.countrygamer.cgo.library.common.register.OptionRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
object CGOOptions extends OptionRegister {

	var enableLambchops: Boolean = true
	var secretPumpkin: Boolean = true

	override def register(): Unit = {
		this.enableLambchops = this.getAndComment(
			"general",
			"Enable Lambchops",
			"Enable Labchops to be dropped from sheep",
			value = true
		)

		this.secretPumpkin = this.getAndComment(
			"general",
			"Secret Pumpkin",
			"Shhhhh!",
			value = true
		)

	}

	@SideOnly(Side.CLIENT)
	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = {
		classOf[GuiConfig]
	}

}
