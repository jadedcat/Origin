package com.countrygamer.countrygamer_core.Items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemSeedBase extends ItemBase implements IPlantable {
	/**
	 * The type of block this seed turns into (wheat or pumpkin stems for
	 * instance)
	 */
	private Block plantBlock;

	/** BlockID of the block the seeds can be planted on. */
	private Block soilBlock;

	public ItemSeedBase(int id, String modid, String name, Block plantBlock,
			Block soil) {
		super(modid, name);
		this.plantBlock = plantBlock;
		this.soilBlock = soil;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		if (par7 != 1) {
			return false;
		} else if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7,
				par1ItemStack)
				&& par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7,
						par1ItemStack)) {
			Block soil = par3World.getBlock(par4, par5, par6);
			//Block  = Block.blocksList[i1];

			if (soil != null
					&& soil.canSustainPlant(par3World, par4, par5, par6,
							ForgeDirection.UP, this)
					&& par3World.isAirBlock(par4, par5 + 1, par6)) {
				par3World.setBlock(par4, par5 + 1, par6, this.plantBlock);
				--par1ItemStack.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world,
			int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return this.plantBlock;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return 0;
	}
}
