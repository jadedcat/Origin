package com.countrygamer.countrygamercore.lib;

import org.lwjgl.input.Keyboard;

public class UtilString {
	
	/**
	 * Used for Item addInformation, NOT game use (ex: player sneaking)
	 * @return
	 */
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}
	
}
