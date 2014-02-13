package com.countrygamer.countrygamer_core.Items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemArmorBase extends ItemArmor {

	public String modid;
	public String armorTex;

	public ItemArmorBase(String modid, String name,
			ItemArmor.ArmorMaterial mat, int slot, int armorType,
			String armorName) {
		super(mat, armorType, slot);
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		// LanguageRegistry.addName(this, name);
		this.setTextureName(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
		this.armorTex = armorName;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot,
			String type) {
		// slot will tell us helmet vs. boots
		// type will be either null or overlay (cloth armor)
		// can use stack.stackTagCompound.getString("matName") for material,
		// etc.
		String layer = "1";
		// String material =
		// stack.stackTagCompound.getString("matName").toLowerCase();
		if (type == null) {
			type = "";
			// all type use the same base texture (which is colorized)
			// material = "iron";
		}
		if (slot == 2) {
			layer = "2";
		}
		// return
		// "artifacts:textures/models/armor/"+material+"_layer_"+layer+type+".png";
		return this.modid + ":textures/models/armor/" + this.armorTex + ".png";
	}

}
