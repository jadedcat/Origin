package com.temportalist.origin.library.client.utility

import com.sun.tools.corba.se.idl.toJavaPortable.Helper
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

	/*
	 * The true ctrl key(29 on mac) on max is used for right clicking
	 */
	private final val shift1: Int = 42
	private final val shift2: Int = 54
	private final val ctrl1: Int = if (Minecraft.isRunningOnMac) 219 else 29
	private final val ctrl2: Int = if (Minecraft.isRunningOnMac) 220 else 157
	private final val alt1: Int = 56
	private final val alt2: Int = 184

	def isShiftKeyDown: Boolean = {
		Keyboard.isKeyDown(Helper.shift1) || Keyboard.isKeyDown(Helper.shift2)
	}

	def isCtrlKeyDown: Boolean = {
		Keyboard.isKeyDown(Helper.ctrl1) || Keyboard.isKeyDown(Helper.ctrl2)
	}

	def isAltKeyDown: Boolean = {
		Keyboard.isKeyDown(Helper.alt1) || Keyboard.isKeyDown(Helper.alt2)
	}

	def isShiftKey(keyCode: Int): Boolean = {
		keyCode == Helper.shift1 || keyCode == Helper.shift2
	}

	def isCtrlKey(keyCode: Int): Boolean = {
		keyCode == Helper.ctrl1 || keyCode == Helper.ctrl2
	}

	def isAltKey(keyCode: Int): Boolean = {
		keyCode == Helper.alt1 || keyCode == Helper.alt2
	}

}
