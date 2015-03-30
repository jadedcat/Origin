package com.temportalist.origin.library.client.gui.config

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.wrapper.client.gui.configFactory.GuiConfigWrapper
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiConfig(guiScreen: GuiScreen) extends GuiConfigWrapper(guiScreen, Origin, Origin.MODID) {

}
