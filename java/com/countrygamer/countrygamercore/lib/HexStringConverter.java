package com.countrygamer.countrygamercore.lib;

import java.io.UnsupportedEncodingException;

/**
 * A Class which will create a hex from string, AND reverse engineer from hex to string
 * 
 * @author EtaYuy88
 * 
 */
public class HexStringConverter {
	private static final String frontwards = "0123456789abcdef";
	// private static final String backwards = "fedcba9876543210";
	
	private static final char[] HEX_CHARS = frontwards.toCharArray();
	private static HexStringConverter hexStringConverter = null;
	
	private HexStringConverter() {
	}
	
	public static HexStringConverter getHexStringConverterInstance() {
		if (hexStringConverter == null) hexStringConverter = new HexStringConverter();
		return hexStringConverter;
	}
	
	public String stringToHex(String input) throws UnsupportedEncodingException {
		if (input == null) throw new NullPointerException();
		return asHex(input.getBytes());
	}
	
	public String hexToString(String txtInHex) {
		byte[] txtInByte = new byte[txtInHex.length() / 2];
		int j = 0;
		for (int i = 0; i < txtInHex.length(); i += 2) {
			txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
		}
		return new String(txtInByte);
	}
	
	private String asHex(byte[] buf) {
		char[] chars = new char[2 * buf.length];
		for (int i = 0; i < buf.length; ++i) {
			chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
			chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
		}
		return new String(chars);
	}
	
}