package com.countrygamer.cgo.wrapper.client.gui.configFactory

import java.util

import cpw.mods.fml.client.config.IConfigElement
import net.minecraftforge.common.config._

/**
 *
 *
 * @author CountryGamer
 */
object GuiConfigHelper {

	def getConfigElements(configuration: Configuration): util.List[IConfigElement[_]] = {
		val elements: util.ArrayList[IConfigElement[_]] = new util.ArrayList[IConfigElement[_]]()

		val iterator: util.Iterator[String] = configuration.getCategoryNames.iterator()
		while (iterator.hasNext) {
			val categoryName: String = iterator.next()
			val cate: ConfigCategory = configuration.getCategory(categoryName)
			val ele: ConfigElement[_] = new ConfigElement[ConfigCategory](cate)
			elements.addAll(ele.getChildElements)
		}

		return elements
	}

}
