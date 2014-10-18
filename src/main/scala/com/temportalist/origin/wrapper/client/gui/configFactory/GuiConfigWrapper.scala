package com.temportalist.origin.wrapper.client.gui.configFactory

import com.temportalist.origin.wrapper.common.PluginWrapper
import cpw.mods.fml.client.config.{GuiConfig, IConfigElement}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiConfigWrapper(guiParent: GuiScreen, mod: PluginWrapper, modid: String) extends GuiConfig(
	guiParent, GuiConfigWrapper.getConfigElements(mod), modid,
	null, false, false, GuiConfigWrapper.getTitle(mod), null
) {

}

@SideOnly(Side.CLIENT)
object GuiConfigWrapper {

	def getConfigElements(plugin: PluginWrapper): java.util.List[IConfigElement[_]] = {
		GuiConfigHelper.getConfigElements(plugin.options.config)
	}

	def getTitle(plugin: PluginWrapper): String = {
		GuiConfig.getAbridgedConfigPath(plugin.options.config.toString)
	}

}
