package com.countrygamer.cgo.client.gui.configFactory

import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.wrapper.client.gui.configFactory.GuiConfigWrapper
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiConfig(guiScreen: GuiScreen) extends GuiConfigWrapper(guiScreen, Origin.pluginID, Origin) {

}
