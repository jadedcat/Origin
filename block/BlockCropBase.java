package com.countrygamer.countrygamer_core.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropBase extends BlockCrops {

	public String modid;
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;
	private final Item seedItem, cropItem;

	public BlockCropBase(int id, String modid, String name, Item seedItem,
			Item cropItem) {
		super();
		this.setBlockName(name);
		GameRegistry.registerBlock(this, name);
		// LanguageRegistry.addName(this, name);

		this.modid = modid;
		this.textureName = name;
		this.seedItem = seedItem;
		this.cropItem = cropItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		/*
		 * this.blockIcon = iconRegister.registerIcon(this.modid + ":" +
		 * this.getUnlocalizedName().substring(
		 * this.getUnlocalizedName().indexOf(".") + 1));
		 */
		this.iconArray = new IIcon[4];

		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = iconRegister.registerIcon(this.modid + ":"
					+ this.getTextureName() + "_" + i);
		}

	}

	public IIcon getIcon(int par1, int par2) {
		if (par2 < 7) {
			if (par2 == 6) {
				par2 = 5;
			}

			return this.iconArray[par2 >> 1];
		} else {
			return this.iconArray[3];
		}
	}

	/**
	 * Generate a seed ItemStack for this crop.
	 */
	@Override
	protected Item func_149866_i() {
		return this.seedItem;
	}

	/**
	 * Generate a crop produce ItemStack for this crop.
	 */
	@Override
	protected Item func_149865_P() {
		return this.cropItem;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified
	 * items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3,
			int par4, int par5, float par6, int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5,
				par6, par7);

		if (!par1World.isRemote) {
			if (par5 >= 7 && par1World.rand.nextInt(50) == 0) {
				this.dropBlockAsItem(par1World, par2, par3, par4,
						new ItemStack(Item.getItemById(394)));
			}
		}
	}

}
