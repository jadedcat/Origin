package com.countrygamer.countrygamercore.common.lib.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {
	
	public static String configGeneral = "General";
	public static String configItemId = "Item IDs";
	public static String configBlockId = "Block IDs";
	public static String configAchievement = "Achievement IDs";
	public static String configAddon = "Addons";

	public static int getAndComment(Configuration config, String cate, String name, String comment,
			int value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals("")) property.comment = comment;
		return property.getInt();
	}
	
	public static String getAndComment(Configuration config, String cate, String name,
			String comment, String value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals("")) property.comment = comment;
		return property.getString();
	}
	
	public static boolean getAndComment(Configuration config, String cate, String name,
			String comment, boolean value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals("")) property.comment = comment;
		return property.getBoolean(false);
	}
	
}
