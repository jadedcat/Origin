package com.temportalist.origin.library.common

import com.temportalist.origin.library.client.gui.config.GuiConfig
import com.temportalist.origin.library.common.register.OptionRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author TheTemportalist
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
