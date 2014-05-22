package com.countrygamer.core.Base.common.item;

import java.util.List;

import com.countrygamer.countrygamercore.lib.UtilKeys;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBase extends Item {
	
	public String modid;
	
	public ItemBase(String modid, String name) {
		super();
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		// LanguageRegistry.addName(this, name);
		this.setTextureName(this.modid + ":" + this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return itemStack;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
			int z, int side, float par8, float par9, float par10) {
		return false;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player,
			EntityLivingBase entity) {
		return false;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
		return false;
	}
	
	public int getMaxStackSize() {
		return this.maxStackSize;
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
	}
	
	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		if (UtilKeys.isShiftKeyDown()) {
			list = this.addInformationWithShift(itemStack, player, list, par4);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	public List addInformationWithShift(ItemStack itemStack, EntityPlayer player, List list,
			boolean par4) {
		return list;
	}
	
}
