package com.temportalist.origin.library.common

import com.temportalist.origin.library.client.gui.config.GuiConfig
import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.register.OptionRegister
import com.temportalist.origin.library.common.utility.WorldHelper
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.audio.SoundCategory
import net.minecraft.client.gui.GuiScreen

import scala.collection.mutable

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
	var volumeControls: mutable.Map[String, Float] = mutable.Map[String, Float]()

	override def register(): Unit = {

		///*
		this.secretPumpkin = this.getAndComment(
			"general",
			"Secret Pumpkin",
			"Shhhhh!",
			value = this.secretPumpkin
		)
		//*/

		if (WorldHelper.isClient())
			this.registerClient()

	}

	@SideOnly(Side.CLIENT)
	def registerClient() {
		///*
		this.coloredHearts = this.getAndComment(
			"client",
			// coloured because hilburn
			"Coloured Hearts",
			"Collapses the vanilla multiple rows of hearts into a colour coded single layer",
			value = this.coloredHearts
		)
		this.heartColors = this.getAndComment(
			"client",
			"Heart Colours",
			"The colors of the hearts. The quantity of colors here represents the tiers of hearts." +
					"(quantity * 20 + 20 = total max health accounted for)",
			value = this.heartColors
		)
		//*/
		///*
		SoundCategory.values().foreach((sound: SoundCategory) => {
			val volume: Float = Rendering.mc.gameSettings.getSoundLevel(sound) * 100f
			this.volumeControls(sound.getCategoryName) = this.getAndComment(
				"client", sound.getCategoryName + " volume", "", volume.toDouble
			).toFloat / 100f
		})
		//*/
		/*
		this.getAndComment(
			"client", "something volume", "", 100d
		)
		*/
		//this.config.get("client", "volume", 100d)

	}

	override def getExtension(): String = "json"

	@SideOnly(Side.CLIENT)
	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = {
		classOf[GuiConfig]
	}

}
