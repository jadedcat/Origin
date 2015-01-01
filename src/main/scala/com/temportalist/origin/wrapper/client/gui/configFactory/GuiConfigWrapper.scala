package com.temportalist.origin.wrapper.client.gui.configFactory

import com.temportalist.origin.wrapper.common.ModWrapper
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.relauncher.{SideOnly, Side}
import net.minecraftforge.fml.client.config.{IConfigElement, GuiConfig}

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

	def getConfigElements(plugin: ModWrapper): java.util.List[IConfigElement] = {
		GuiConfigHelper.getConfigElements(plugin.options.config)
	}

	def getTitle(plugin: ModWrapper): String = {
		GuiConfig.getAbridgedConfigPath(plugin.options.config.toString)
	}

}
