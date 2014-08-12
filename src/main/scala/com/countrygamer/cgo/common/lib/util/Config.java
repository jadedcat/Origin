package com.countrygamer.cgo.common.lib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.List;

public class Config {

	public static String configGeneral = "General";
	public static String configItemId = "Item IDs";
	public static String configBlockId = "Block IDs";
	public static String configAchievement = "Achievement IDs";
	public static String configAddon = "Addons";

	public static int getAndComment(Configuration config, String cate, String name, String comment,
			int value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getInt();
	}

	public static String getAndComment(Configuration config, String cate, String name,
			String comment, String value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getString();
	}

	public static boolean getAndComment(Configuration config, String cate, String name,
			String comment, boolean value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getBoolean(false);
	}

	public static double getAndComment(Configuration config, String cate, String name,
			String comment, double value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getDouble();
	}

	public static boolean[] getAndComment(Configuration config, String cate, String name,
			String comment, boolean[] value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getBooleanList();
	}

	public static int[] getAndComment(Configuration config, String cate, String name,
			String comment, int[] value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getIntList();
	}

	public static double[] getAndComment(Configuration config, String cate, String name,
			String comment, double[] value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getDoubleList();
	}

	public static String[] getAndComment(Configuration config, String cate, String name,
			String comment, String[] value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getStringList();
	}

	public static List<Item> getItemsFromArray(String[] names) {
		List<Item> ret = new ArrayList<Item>();
		for (String name : names) {
			if (name != null) {
				Object obj = Item.itemRegistry.getObject(name);
				if (obj != null && obj instanceof Item) {
					ret.add((Item) obj);
				}
			}
		}
		return ret;
	}

	public static List<Block> getBlocksFromArray(String[] names) {
		List<Block> ret = new ArrayList<Block>();
		for (String name : names) {
			if (name != null) {
				Block obj = Block.getBlockFromName(name);
				if (obj != null) {
					ret.add(obj);
				}
			}
		}
		return ret;
	}

}
