package com.countrygamer.countrygamercore.common.item;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.countrygamer.countrygamercore.common.Core;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemArmorBase extends ItemArmor {
	
	public static Item[] createArmors(String modid, String[] names, ItemArmor.ArmorMaterial mat,
			int renderIndex, boolean addToTab) {
		Item[] ret = new Item[4];
		for (int i = 0; i < 4; i++) {
			ret[i] = new ItemArmorBase(modid, names[i], mat, renderIndex, getArmorStringFromType(i));
			if (addToTab) Core.addItemToTab(ret[i]);
		}
		return ret;
	}
	
	public String modid;
	
	public ItemArmorBase(String modid, String name, ItemArmor.ArmorMaterial mat, int renderIndex,
			String type) {
		super(mat, renderIndex, getArmorTypeFromString(type));
		
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		this.setTextureName(this.modid + ":" + this.getUnlocalizedName().substring(5));
		
	}
	
	public static final String[] armor_types = new String[] {
			"Helmet", "Chestplate", "Pants", "Boots"
	};
	
	public static int getArmorTypeFromString(String type) {
		for (int i = 0; i < armor_types.length; i++) {
			if (type.equalsIgnoreCase(armor_types[i])) return i;
		}
		return -1;
	}
	
	public static String getArmorStringFromType(int type) {
		return type >= 0 && type < armor_types.length ? armor_types[type] : "";
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@SideOnly(Side.CLIENT)
	public List addInformationWithShift(ItemStack itemStack, EntityPlayer player, List list,
			boolean par4) {
		list.set(0, list.get(0) + " (" + getArmorStringFromType(this.armorType) + ")");
		return list;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		int layer = slot == 2 ? 2 : 1;
		return this.modid + ":textures/model/armor/"
				+ RenderBiped.bipedArmorFilenamePrefix[this.renderIndex] + "_layer_" + layer
				+ ".png";
	}
	
}
