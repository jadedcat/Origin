package com.countrygamer.core.Base.common.item;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMetadataBase extends ItemBase {

	public final String[] metaNames;

	@SideOnly(Side.CLIENT)
	private IIcon[] metaIcons;

	public ItemMetadataBase(String modid, String[] names) {
		super(modid, names[0]);
		this.setUnlocalizedName(names[0]);
		this.metaNames = names;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		int j = MathHelper.clamp_int(par1, 0, this.metaNames.length - 1);
		return this.metaIcons[j];
	}

	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0,
				this.metaNames.length - 1);
		return super.getUnlocalizedName() + "." + metaNames[i];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int j = 0; j < this.metaNames.length; ++j) {
			par3List.add(new ItemStack(this, 1, j));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconReg) {
		this.metaIcons = new IIcon[this.metaNames.length];
		for (int i = 0; i < this.metaNames.length; ++i) {
			this.metaIcons[i] = iconReg.registerIcon(this.modid + ":"
					+ metaNames[i]);
		}
	}

	public static int getIndex(String[] names, String name) {
		return Arrays.asList(names).indexOf(name);
	}

}
