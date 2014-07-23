package com.countrygamer.cgo.wrapper.client.gui.configFactory

import java.util

import com.countrygamer.cgo.wrapper.common.registries.OptionRegister
import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
class GuiFactoryWrapper(val options: OptionRegister) extends IModGuiFactory {

	override def initialize(minecraftInstance: Minecraft) {
	}

	override def runtimeGuiCategories(): util.Set[RuntimeOptionCategoryElement] = {
		return null
	}

	override def getHandlerFor(
			element: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler = {
		return null
	}

	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = {
		return options.getGuiConfigClass()
	}

}
