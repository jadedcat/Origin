package com.countrygamer.cgo.common.lib.util;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class General {

	public static HashMap<ItemStack, ItemStack> getBasicOreDict() {
		HashMap<ItemStack, ItemStack> oreDict = new HashMap<ItemStack, ItemStack>();
		String[] oreNames = new String[] {
				"Coal", "Iron", "Gold", "Lapis", "Redstone", "Diamond", "Emerald", "Copper",
				"Silver", "Tin", "Bronze", "Lead"
		};
		oreDict.put(new ItemStack(Blocks.coal_ore, 1), new ItemStack(Items.coal, 1));
		oreDict.put(new ItemStack(Blocks.iron_ore, 1), new ItemStack(Items.iron_ingot, 1));
		oreDict.put(new ItemStack(Blocks.gold_ore, 1), new ItemStack(Items.gold_ingot, 1));
		oreDict.put(new ItemStack(Blocks.diamond_ore, 1), new ItemStack(Items.diamond, 1));
		oreDict.put(new ItemStack(Blocks.lapis_ore, 1), new ItemStack(Items.dye, 1, 4));
		oreDict.put(new ItemStack(Blocks.redstone_ore, 1), new ItemStack(Items.redstone, 1));
		oreDict.put(new ItemStack(Blocks.emerald_ore, 1), new ItemStack(Items.emerald, 1));

		for (String oreName : oreNames) {
			ArrayList<ItemStack> ores = OreDictionary.getOres("ore" + oreName);
			for (ItemStack ore : ores) {
				ArrayList<ItemStack> ingots = OreDictionary.getOres("ingot" + oreName);
				if (!ingots.isEmpty()) {
					oreDict.put(ore, ingots.get(0));
				}
			}
		}

		return oreDict;
	}

	/**
	 * Find new id
	 *
	 * @return A unique entity ID
	 */
	public static int getUniqueEntityId() {
		int entityid = 0;
		do {
			entityid += 1;
		} while (EntityList.getStringFromID(entityid) != null);
		return entityid;
	}

	/**
	 * Check for loaded mod
	 *
	 * @param targetModid the modid of the mod you are looking for
	 * @return whether or not the mod is installed
	 */
	public static boolean isModLoaded(String targetModid) {
		if (Loader.isModLoaded(targetModid)) {
			try {
				// System.out.println(sourceModid + ": " + targetModid
				// + " mod is loaded");
				return true;
			} catch (Exception e) {
				// System.err.println(sourceModid + ": Could not load "
				// + targetModid + " mod");
				e.printStackTrace(System.err);
			}
		}
		return false;
	}

	public static int getNewEntityID() {
		int id = 0;
		while (EntityList.getStringFromID(id) != null)
			id++;
		return id;
	}

	public static int getNewBiomeID() {
		int id;
		for (id = 0; id < BiomeGenBase.getBiomeGenArray().length; id++) {
			if (BiomeGenBase.getBiomeGenArray()[id] == null) {
				break;
			}
		}
		return id;
	}

}
