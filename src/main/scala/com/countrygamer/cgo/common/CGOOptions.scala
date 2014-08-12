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
		this.enableLambchops = Config.getAndComment(this.config, "general",
			"Enable Lambchops",
			"Enable Labchops to be dropped from sheep",
			true
		)

		this.secretPumpkin = Config.getAndComment(this.config, "general",
			"Secret Pumpkin",
			"Shhhhh!",
			true
		)

		val a1: Int = Config.getAndComment(this.config, "A Cat", "a1", "comment", 1)
		val a2: Int = Config.getAndComment(this.config, "A Cat", "a2", "comment", 2)
		val b1: Int = Config.getAndComment(this.config, "B Cat", "b1", "comment", 3)
		val b2: Int = Config.getAndComment(this.config, "B Cat", "b2", "comment", 4)
		val c1: Array[Boolean] = Config.getAndComment(this.config, "C Cate", "c1", "comment",
			Array[Boolean](true, false, true))
		val c2: Array[Int] = Config
				.getAndComment(this.config, "C Cate", "c2", "comment", Array[Int](1, 2, 3))
		val c3: Array[Double] = Config
				.getAndComment(this.config, "C Cate", "c3", "comment", Array[Double](1.1, 2.2, 3.3))
		val c4: Array[String] = Config
				.getAndComment(this.config, "C Cate", "c4", "comment", Array[String]("x", "y", "z"))

	}

	@SideOnly(Side.CLIENT)
	override def getGuiConfigClass(): Class[_ <: GuiScreen] = {
		classOf[com.countrygamer.cgo.client.gui.configFactory.GuiConfig]
	}

}
