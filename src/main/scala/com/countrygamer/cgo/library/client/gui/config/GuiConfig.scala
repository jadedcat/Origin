package com.countrygamer.cgo.library.client.gui.config

import com.countrygamer.cgo.library.common.Origin
import com.countrygamer.cgo.wrapper.client.gui.configFactory.GuiConfigWrapper
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiConfig(guiScreen: GuiScreen) extends GuiConfigWrapper(guiScreen, Origin, Origin.pluginID) {

}
