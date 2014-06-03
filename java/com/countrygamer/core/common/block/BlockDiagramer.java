package com.countrygamer.core.common.block;

import com.countrygamer.core.common.lib.CoreReference;
import com.countrygamer.core.common.tileentity.TileEntityDiagramer;
import com.countrygamer.countrygamercore.Base.common.block.BlockContainerBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by dustinyost on 3/21/14.
 */
public class BlockDiagramer extends BlockContainerBase {

	public BlockDiagramer (
			Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}

	@Override
	public void breakBlock(
			World world, int x, int y, int z, Block par5, int par6) {
		TileEntityDiagramer tileEnt = (TileEntityDiagramer) world.getTileEntity(x, y, z);
		Random rand = new Random();

		if (tileEnt != null) {
			for (int j1 = 0; j1 < tileEnt.getSizeInventory(); j1++) {
				ItemStack itemstack = tileEnt.getStackInSlot(j1);
				if (itemstack != null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					entityitem = new EntityItem(world,
							(double) ((float) x + f),
							(double) ((float) y + f1),
							(double) ((float) z + f2), itemstack.copy());
					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) rand.nextGaussian()
							* f3 + 0.2F);
					entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}
					world.spawnEntityInWorld(entityitem);
				}
			}
			world.func_147453_f(x, y, z, par5);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockActivated(
			World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		if (!player.isSneaking()) {
			player.openGui(CoreReference.MOD_ID, CoreReference.diagramerGui, world, x, y, z);
			return true;
		}
		return false;
	}

}
