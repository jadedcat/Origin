package com.countrygamer.countrygamercore.Base.Plugin;

import net.minecraft.entity.EntityList;
import net.minecraft.world.biome.BiomeGenBase;

public class PluginUtility {
	
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
