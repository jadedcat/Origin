package com.countrygamer.cgo.common

import com.countrygamer.cgo.common.lib.util.Config
import com.countrygamer.cgo.wrapper.common.registries.OptionRegister
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
		this.enableLambchops = Config.getAndComment(this.config, "Other",
			"Enable Lambchops",
			"Enable Labchops to be dropped from sheep",
			true
		)

		this.secretPumpkin = Config.getAndComment(this.config, "Other",
			"Secret Pumpkin",
			"Shhhhh!",
			true
		)

	}

	@SideOnly(Side.CLIENT)
	override def getGuiConfigClass(): Class[_ <: GuiScreen] = {
		classOf[com.countrygamer.cgo.client.gui.configFactory.GuiConfig]
	}

}
