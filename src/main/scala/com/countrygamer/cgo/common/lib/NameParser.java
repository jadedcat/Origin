package com.countrygamer.cgo.common.lib;

import com.google.gson.JsonArray;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;

/**
 * @author CountryGamer
 */
public class NameParser {

	public static String getName(ItemStack itemStack) {
		String name;

		if (itemStack == null) {
			return "";
		}

		Item item = itemStack.getItem();
		// is item
		if (Block.getBlockFromItem(item) == Blocks.air) {
			name = GameData.getItemRegistry().getNameForObject(item);
		}
		// is block
		else {
			name = GameData.getBlockRegistry()
					.getNameForObject(Block.getBlockFromItem(item));
		}

		return name;
	}

	public static ItemStack getItemStack(String name) {
		int endNameIndex = name.length();
		int metadata = OreDictionary.WILDCARD_VALUE;
		if (name.matches("(.*):(.*):(.*)")) {
			endNameIndex = name.lastIndexOf(':');
			metadata = Integer.parseInt(name.substring(endNameIndex + 1, name.length()));
		}
		String modid = name.substring(0, name.indexOf(':'));
		String itemName = name.substring(name.indexOf(':') + 1, endNameIndex);
		ItemStack itemStack = GameRegistry.findItemStack(modid, itemName, 1);
		if (itemStack != null)
			itemStack.setItemDamage(metadata);
		return itemStack;
	}

	public static boolean isInList(ItemStack itemStack, HashSet<String> list) {
		String itemStack_nonMetadata, itemStack_fullName;

		if (itemStack == null)
			return false;

		itemStack_nonMetadata = NameParser.getName(itemStack);
		itemStack_fullName = itemStack_nonMetadata + ":" + itemStack.getItemDamage();

		boolean item_is_in_list = list.contains(itemStack_fullName);
		if (!item_is_in_list) {
			item_is_in_list = list.contains(itemStack_nonMetadata);
		}

		return item_is_in_list;
	}

	public static void registerRecipe(JsonArray jsonArray) {
		ItemStack result = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonArray parts = jsonArray.get(i).getAsJsonArray();
			result = NameParser.getItemStack(parts.get(0).getAsString());

			boolean isShaped = false;
			Object[] objs = new Object[parts.size() - 1];

			for (int i1 = 1; i1 < parts.size(); i1++) {
				int j = i1 - 1;
				String comp = parts.get(i1).getAsString();
				if (comp.matches("(.*):(.*)")) {
					objs[j] = NameParser.getItemStack(comp);
				}
				else if (comp.length() == 1 && j > 1) {
					objs[j] = comp.charAt(0);
				}
				else {
					objs[j] = comp;
					isShaped = true;
				}

			}

			if (isShaped) {
				GameRegistry.addShapedRecipe(result, objs);
			}
			else {
				GameRegistry.addShapelessRecipe(result, objs);
			}

		}
	}

}
