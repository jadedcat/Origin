package com.temportalist.origin.wrapper.client.gui.configFactory

import java.util

import com.temportalist.origin.library.common.utility.Scala
import com.temportalist.origin.wrapper.common.ModWrapper
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.{ConfigElement, Configuration}
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement
import net.minecraftforge.fml.client.config._
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiConfigWrapper(guiParent: GuiScreen, mod: ModWrapper, modid: String) extends GuiConfig (
	guiParent, GuiConfigWrapper.getConfigElements(mod), modid,
	null, false, false, GuiConfigWrapper.getTitle(mod), null
) {

}

@SideOnly(Side.CLIENT)
object GuiConfigWrapper {

	def getTitle(plugin: ModWrapper): String = {
		GuiConfig.getAbridgedConfigPath(plugin.options.config.toString)
	}

	def getConfigElements(plugin: ModWrapper): java.util.List[IConfigElement] = {
		GuiConfigWrapper.getConfigElements(plugin.options.config)
	}

	def getConfigElements(configuration: Configuration): util.List[IConfigElement] = {
		val elements: util.List[IConfigElement] = new util.ArrayList[IConfigElement]()

		Scala.foreach(configuration.getCategoryNames, (categoryName: String) => {
			val category: ConfigElement = new ConfigElement(configuration.getCategory(categoryName))
			if (categoryName.equals(Configuration.CATEGORY_GENERAL)) {
				elements.addAll(category.getChildElements)
			}
			else {
				elements.add(new DummyCategoryElement(categoryName, categoryName, category.getChildElements))
			}
		})

		elements
	}

}
