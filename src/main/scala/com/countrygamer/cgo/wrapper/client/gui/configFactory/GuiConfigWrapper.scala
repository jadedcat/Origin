package com.countrygamer.cgo.wrapper.client.gui.configFactory

import com.countrygamer.cgo.wrapper.common.PluginWrapper
import cpw.mods.fml.client.config.GuiConfig
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiConfigWrapper(guiScreen: GuiScreen, pluginID: String, plugin: PluginWrapper)
		extends GuiConfig(guiScreen, GuiConfigHelper.getConfigElements(plugin.options.config),
			pluginID, false, false,
			GuiConfig.getAbridgedConfigPath(plugin.options.config.toString)) {

}
