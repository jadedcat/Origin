package com.temportalist.origin.library.common

import com.temportalist.origin.library.client.gui.config.GuiConfig
import com.temportalist.origin.library.common.register.OptionRegister
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author TheTemportalist
 */
object CGOOptions extends OptionRegister {

	var secretPumpkin: Boolean = true

	override def register(): Unit = {
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
