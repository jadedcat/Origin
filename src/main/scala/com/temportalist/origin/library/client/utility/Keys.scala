package com.temportalist.origin.library.client.utility

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import org.lwjgl.input.Keyboard

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object Keys {

	def keyPressed_Shift_Gui(): Boolean = {
		Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)
	}

	def keyPressed_Ctrl_Gui(): Boolean = {
		if (Minecraft.isRunningOnMac)
			Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)
		else
			Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)
	}

	def keyPressed_Alt_Gui(): Boolean = {
		Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184)
	}

}
