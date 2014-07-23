package com.countrygamer.cgo.common.lib.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class UtilKeys {

	/**
	 * Used for Item addInformation, NOT game use (ex: player sneaking)
	 *
	 * @return
	 */
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}

	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ?
				Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) :
				Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	public static boolean isAltKeyDown() {
		return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
	}

}
