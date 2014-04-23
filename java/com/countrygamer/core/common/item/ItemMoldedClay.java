package com.countrygamer.core.common.item;

import com.countrygamer.core.Base.common.item.ItemBase;
import com.countrygamer.core.common.lib.CoreReference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by CountryGamer on 3/18/14.
 */
public class ItemMoldedClay extends ItemBase {

	public static String recipieNameKey = CoreReference.MOD_ID + "_RecipieName";

	public ItemMoldedClay (String modid, String name) {
		super(modid, name);
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemStack itemStack1 = itemStack.copy();
		if (itemStack1.getItem() instanceof ItemMoldedClay && player.isSneaking()) {
			itemStack1 = new ItemStack(Items.clay_ball, itemStack1.getItemDamage(), itemStack1.stackSize);
		}
		return itemStack1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		ItemStack itemStack1 = itemStack.copy();
		if (itemStack1.getItem() instanceof ItemMoldedClay) {
			String name = itemStack1.getTagCompound().getString(ItemMoldedClay.recipieNameKey);
			if (!name.equals("")) {
				list.add(name);
			}
		}
	}

}
