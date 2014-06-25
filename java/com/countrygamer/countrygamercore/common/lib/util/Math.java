package com.countrygamer.countrygamercore.common.lib.util;

import java.util.Random;

public class Math {
	
	/**
	 * Is int positive or negative
	 * 
	 * @param i
	 * @return
	 */
	public static int posOrNeg(int i) {
		if (i == 0) {
			System.err.print("Parameter is neither positive nor negative");
			return 1;
		}
		else if (i >> 31 != 0)
			return -1;
		else
			return 1;
	}
	
	public static boolean chance(int percent) {
		return (new Random()).nextInt(100) < percent;
	}
	
}
