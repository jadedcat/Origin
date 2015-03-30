package com.temportalist.origin.library.client.utility

import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object GuiHelper {

	def display(gui: GuiScreen): Unit = Minecraft.getMinecraft.displayGuiScreen(gui)

}
