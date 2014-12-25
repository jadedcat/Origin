package com.temportalist.origin.wrapper.client.gui.configFactory

import com.temportalist.origin.wrapper.common.PluginWrapper
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.relauncher.{SideOnly, Side}
import net.minecraftforge.fml.client.config.{IConfigElement, GuiConfig}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiConfigWrapper(guiParent: GuiScreen, mod: PluginWrapper, modid: String) extends GuiConfig (
	guiParent, GuiConfigWrapper.getConfigElements(mod), modid,
	null, false, false, GuiConfigWrapper.getTitle(mod), null
) {

}

@SideOnly(Side.CLIENT)
object GuiConfigWrapper {

	def getConfigElements(plugin: PluginWrapper): java.util.List[IConfigElement] = {
		GuiConfigHelper.getConfigElements(plugin.options.config)
	}

	def getTitle(plugin: PluginWrapper): String = {
		GuiConfig.getAbridgedConfigPath(plugin.options.config.toString)
	}

}
