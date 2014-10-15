package com.countrygamer.cgo.wrapper.client.gui.configFactory;

import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
public class GuiConfigHelper {

	public static List<IConfigElement> getConfigElements(Configuration configuration) {
		List<IConfigElement> elements = new ArrayList<IConfigElement>();

		ConfigCategory generalCate = configuration.getCategory(Configuration.CATEGORY_GENERAL);
		ConfigElement<ConfigCategory> generalElement = new ConfigElement<ConfigCategory>(
				generalCate);
		elements.addAll(generalElement.getChildElements());

		for (String categoryName : configuration.getCategoryNames()) {
			if (Configuration.CATEGORY_GENERAL.equals(categoryName))
				continue;

			ConfigCategory category = configuration.getCategory(categoryName);
			ConfigElement<ConfigCategory> categoryElement = new ConfigElement<ConfigCategory>(
					category);

			List<IConfigElement> categoryList = new ArrayList<IConfigElement>();

			for (IConfigElement elementInCate : categoryElement.getChildElements()) {

				String elementInCateValue = (String) elementInCate.get();

				DummyConfigElement element = null;

				ConfigGuiType type = elementInCate.getType();

				String name = elementInCate.getName();
				String comment = "config." + elementInCate.getName();

				switch (type) {
					case BOOLEAN:
						if (!elementInCate.isList()) {
							element = new DummyConfigElement<Boolean>(
									name,
									Boolean.parseBoolean(elementInCateValue),
									type, comment);
						}
						else {
							Object[] vals = elementInCate.getList();
							element = new DummyConfigElement.DummyListElement<Boolean>(
									name,
									Arrays.copyOf(vals, vals.length, Boolean[].class),
									type, comment);
						}
						break;
					case DOUBLE:
						if (!elementInCate.isList()) {
							element = new DummyConfigElement<Double>(
									name,
									Double.parseDouble(elementInCateValue),
									type, comment);
						}
						else {
							Object[] vals = elementInCate.getList();
							element = new DummyConfigElement.DummyListElement<Double>(
									name,
									Arrays.copyOf(vals, vals.length, Double[].class),
									type, comment);
						}
						break;
					case INTEGER:
						if (!elementInCate.isList()) {
							element = new DummyConfigElement<Integer>(
									name,
									Integer.parseInt(elementInCateValue),
									type, comment);
						}
						else {
							Object[] vals = elementInCate.getList();
							element = new DummyConfigElement.DummyListElement<Integer>(
									name,
									Arrays.copyOf(vals, vals.length, Integer[].class),
									type, comment);
						}
						break;
					case STRING:
						if (!elementInCate.isList()) {
							element = new DummyConfigElement<String>(
									name,
									elementInCateValue,
									type, comment);
						}
						else {
							Object[] vals = elementInCate.getList();
							element = new DummyConfigElement.DummyListElement<String>(
									name,
									Arrays.copyOf(vals, vals.length, String[].class),
									type, comment);
						}
						break;
					default:

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
