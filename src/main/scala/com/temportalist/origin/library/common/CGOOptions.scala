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
	var coloredHearts: Boolean = true
	var heartColors: Array[String] = Array[String](
		"#ff1313",
		"#f26c00", "#eabd00", "#00ce00", "#0097ed", "#aa7eff",
		"#ea77fb", "#fb77a0", "#fbd177", "#fbd177", "#fbd177",
		"#fbd177"
	)

	override def register(): Unit = {

		this.secretPumpkin = this.getAndComment(
			"general",
			"Secret Pumpkin",
			"Shhhhh!",
			value = this.secretPumpkin
		)
		this.coloredHearts = this.getAndComment(
			"general",
			// coloured because hilburn
			"Coloured Hearts",
			"Collapses the vanilla multiple rows of hearts into a colour coded single layer",
			value = this.coloredHearts
		)
		this.heartColors = this.getAndComment(
			"general",
			"Hear Colours",
			"The colors of the hearts. The quantity of colors here represents the tiers of hearts." +
					"(quantity * 20 + 20 = total max health accounted for)",
			value = this.heartColors
		)

	}

	override def getExtension(): String = "json"

	@SideOnly(Side.CLIENT)
	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = {
		classOf[GuiConfig]
	}

}
