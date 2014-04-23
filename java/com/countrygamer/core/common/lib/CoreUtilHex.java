package com.countrygamer.core.common.lib;

import java.text.DecimalFormat;
import java.util.HashMap;

import net.minecraft.item.ItemStack;

public class CoreUtilHex {
	
	/**
	 * Returns color ratios for each RR GG and BB values of a hex string, determined from an item
	 * 
	 * @param item
	 * @return
	 */
	public static double[] getColorFromDye(ItemStack itemStack) {
		HashMap<Integer, String> hexFromMeta = new HashMap<Integer, String>();
		hexFromMeta.put(0, "000000");
		hexFromMeta.put(1, "ff0000");
		hexFromMeta.put(2, "00ff00");
		hexFromMeta.put(3, "502800");
		hexFromMeta.put(4, "0000ff");
		hexFromMeta.put(5, "640064");
		hexFromMeta.put(6, "006464");
		hexFromMeta.put(7, "bababa");
		hexFromMeta.put(8, "6b6b6b");
		hexFromMeta.put(9, "ff00ff");
		hexFromMeta.put(10, "006400");
		hexFromMeta.put(11, "ffff00");
		hexFromMeta.put(12, "00aaff");
		hexFromMeta.put(13, "e600e6");
		hexFromMeta.put(14, "ff9000");
		hexFromMeta.put(15, "ffffff");
		
		double[] sums = getSumHex(hexFromMeta.containsKey(itemStack.getItemDamage()) ? hexFromMeta
				.get(itemStack.getItemDamage()) : "");
		// MultiDye.log.info(sums[0] + ":" + sums[1] + ":" + sums[2]);
		return sums;
	}
	
	/**
	 * Returns int[] of the sum of each RR GG and BB values in a hex string
	 * Returns {-1, -1, -1} if impossible
	 * 
	 * @param hexString
	 * @return
	 */
	public static double[] getSumHex(String hexString) {
		if (isValidHexString(hexString)) {
			// split into 3, 2 character, strings. Each character has value 0-f to
			// be translated
			String r = "", g = "", b = "";
			char[] chars = hexString.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (i < 2)
					r += chars[i];
				else if (i < 4)
					g += chars[i];
				else if (i < 6) b += chars[i];
			}
			
			double rSum = formatToDecimals(getSumIndividualHex(r), 2, 2) / 30.00D;
			double gSum = formatToDecimals(getSumIndividualHex(g), 2, 2) / 30.00D;
			double bSum = formatToDecimals(getSumIndividualHex(b), 2, 2) / 30.00D;
			
			rSum = formatToDecimals(rSum, 2, 2);
			gSum = formatToDecimals(gSum, 2, 2);
			bSum = formatToDecimals(bSum, 2, 2);
			
			return new double[] {
					rSum, gSum, bSum
			};
		}
		return new double[] {
				-1, -1, -1
		};
	}
	
	/**
	 * Returns sum of RR GG or BB string values
	 * 
	 * @param digitsHex
	 * @return
	 */
	public static double getSumIndividualHex(String digitsHex) {
		double sum = 0;
		
		HashMap<String, Integer> strToInt = getHexValues();
		
		char[] chars = digitsHex.toCharArray();
		for (char letter : chars) {
			String str = letter + "";
			sum += strToInt.get(str.toLowerCase());
		}
		
		return sum;
	}
	
	public static HashMap<String, Integer> getHexValues() {
		HashMap<String, Integer> strToInt = new HashMap<String, Integer>();
		strToInt.put("0", 0);
		strToInt.put("1", 1);
		strToInt.put("2", 2);
		strToInt.put("3", 3);
		strToInt.put("4", 4);
		strToInt.put("5", 5);
		strToInt.put("6", 6);
		strToInt.put("7", 7);
		strToInt.put("8", 8);
		strToInt.put("9", 9);
		strToInt.put("a", 10);
		strToInt.put("b", 11);
		strToInt.put("c", 12);
		strToInt.put("d", 13);
		strToInt.put("e", 14);
		strToInt.put("f", 15);
		return strToInt;
	}
	
	/**
	 * Returns hexadecimal integer value of integer RGB values.
	 * Returns -1 if impossible;
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int convertRGBtoHex(int r, int g, int b) {
		int hex = -1;
		try {
			hex = Integer.parseInt(convertRGBtoHexString(r, g, b));
		} catch (NumberFormatException e) {
			return 0;
		}
		return hex;
	}
	
	/**
	 * Returns hexadecimal string value of integer RGB values.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static String convertRGBtoHexString(int r, int g, int b) {
		return String.format("%02x%02x%02x", r, g, b);
	}
	
	/**
	 * 
	 * @param hexString
	 *            EX; "ff80a5"
	 * @return int[]{R, G, B}
	 */
	public static int[] convertHexToRGB(String hexString) {
		return new int[] {
				Integer.valueOf(hexString.substring(0, 2), 16),
				Integer.valueOf(hexString.substring(2, 4), 16),
				Integer.valueOf(hexString.substring(4, 6), 16)
		};
	}
	
	public static boolean isValidHexString(String str) {
		if (str.length() != 6) return false;
		boolean valid = false;
		char[] chars = str.toCharArray();
		HashMap<String, Integer> hexVals = getHexValues();
		for (int i = 0; i < chars.length; i++) {
			valid = hexVals.get(chars[i] + "") != null;
			if (!valid) return valid;
		}
		return true;
	}
	
	public static double formatToDecimals(double number, int maxInt, int decimals) {
		String decimal = "";
		for (int i = 0; i < maxInt; i++) {
			decimal += "#";
		}
		decimal += ".";
		for (int i = 0; i < decimals; i++) {
			decimal += "#";
		}
		
		DecimalFormat df = new DecimalFormat(decimal);
		return Double.parseDouble(df.format(number));
		
	}
	
}
