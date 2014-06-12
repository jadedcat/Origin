package com.countrygamer.countrygamercore.Base.common.item_old;

import net.minecraft.item.ItemFood;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemFoodBase extends ItemFood {

	public String modid;

	public ItemFoodBase(int id, String modid, String name, int healAmount, float saturationModifier, boolean isWolfFavorite) {
		// id, healAmount, saturationModifier, isWolf'sFavorite
		super(healAmount, saturationModifier, isWolfFavorite);
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		//LanguageRegistry.addName(this, name);
		this.setTextureName(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
	}

}
