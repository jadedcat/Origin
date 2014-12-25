package com.temportalist.origin.wrapper.client.gui.configFactory;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
public class GuiConfigHelper {

	public static List<IConfigElement> getConfigElements(Configuration configuration) {
		List<IConfigElement> elements = new ArrayList<IConfigElement>();

		ConfigCategory generalCate = configuration.getCategory(Configuration.CATEGORY_GENERAL);
		ConfigElement generalElement = new ConfigElement(
				generalCate);
		elements.addAll(generalElement.getChildElements());

		for (String categoryName : configuration.getCategoryNames()) {
			if (Configuration.CATEGORY_GENERAL.equals(categoryName))
				continue;

			ConfigCategory category = configuration.getCategory(categoryName);
			ConfigElement categoryElement = new ConfigElement(
					category);

			List<IConfigElement> categoryList = new ArrayList<IConfigElement>();

			for (IConfigElement elementInCate : categoryElement.getChildElements()) {

				String elementInCateValue = (String) elementInCate.get();

				DummyConfigElement element = null;

				ConfigGuiType type = elementInCate.getType();

				String name = elementInCate.getName();
				String comment = "config." + elementInCate.getName();

				if (!elementInCate.isList()) {
					element = new DummyConfigElement(
							name,
							Boolean.parseBoolean(elementInCateValue),
							type, comment);
				}
				else {
					Object[] vals = elementInCate.getList();
					element = new DummyConfigElement.DummyListElement(
							name,
							Arrays.copyOf(vals, vals.length, vals.getClass()),
							type, comment);
				}

				categoryList.add(element);

			}

			elements.add(new DummyConfigElement.DummyCategoryElement(categoryName, categoryName,
					categoryList));
		}

		return elements;
	}

	/*

	def getConfigElements(configuration: Configuration): util.List[IConfigElement[_]] = {
		val elements: util.ArrayList[IConfigElement[_]] = new util.ArrayList[IConfigElement[_]]()

		val generalCate: ConfigCategory = configuration.getCategory(Configuration.CATEGORY_GENERAL)
		val generalElement: ConfigElement[_] = new ConfigElement[ConfigCategory](generalCate)
		elements.addAll(generalElement.getChildElements)

		val iterator: util.Iterator[String] = configuration.getCategoryNames.iterator()
		while (iterator.hasNext) {
			val categoryName: String = iterator.next()
			if (!Configuration.CATEGORY_GENERAL.equals(categoryName)) {
				val cate: ConfigCategory = configuration.getCategory(categoryName)
				val ele: ConfigElement[_] = new ConfigElement[ConfigCategory](cate)

				val categoryList: util.ArrayList[IConfigElement[_]] = new util.ArrayList[IConfigElement[_]]()

				val cateElementIterator: util.Iterator[IConfigElement[_]] = ele.getChildElements
						.iterator()
				while (cateElementIterator.hasNext) {
					val cateElement: IConfigElement[_] = cateElementIterator.next()

				}

				elements.add(new DummyConfigElement[_](categoryName, categoryName, categoryList))
			}
		}

		elements
	}

	 */

}
