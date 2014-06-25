package com.countrygamer.countrygamercore.common.lib.util;

import org.lwjgl.input.Keyboard;

public class UtilKeys {
	
	/**
	 * Used for Item addInformation, NOT game use (ex: player sneaking)
	 * @return
	 */
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}
	
}
