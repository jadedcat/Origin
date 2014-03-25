package com.countrygamer.core.Base.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemToolBase extends ItemTool {

	public String modid;

	public ItemToolBase(String modid, String name, float damageVsEntity,
			Item.ToolMaterial material, Block[] validBlocks) {
		super(damageVsEntity, material, Sets.newHashSet(validBlocks));
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		// LanguageRegistry.addName(this, name);
		this.setTextureName(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
	}

}