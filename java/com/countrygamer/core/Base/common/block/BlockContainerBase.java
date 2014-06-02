package com.countrygamer.core.Base.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.core.Base.common.tileentity.TileEntityBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class BlockContainerBase extends BlockBase implements ITileEntityProvider {
	
	public Class<? extends TileEntity> tileEntityClass;
	
	public BlockContainerBase(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		this(mat, modid, name, tileEntityClass, null);
		
	}
	
	public BlockContainerBase(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass, Class<? extends ItemBlock> item) {
		super(mat, modid, name, item);
		this.isBlockContainer = true;
		this.tileEntityClass = tileEntityClass;
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		try {
			return (TileEntity) this.tileEntityClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new TileEntityInventoryBase("", 0, 0);
	}
	
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		this.checkPower(world, x, y, z);
		super.onBlockAdded(world, x, y, z);
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		this.checkPower(world, x, y, z);
	}
	
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.checkPower(world, x, y, z);
	}
	
	private void checkPower(World world, int x, int y, int z) {
		if (!world.isRemote) {
			TileEntity tEnt = world.getTileEntity(x, y, z);
			if (tEnt instanceof TileEntityBase) {
				TileEntityBase tileEnt = (TileEntityBase) tEnt;
				if (tileEnt.isPowered() && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
					tileEnt.setPowered(false);
				}
				else if (!tileEnt.isPowered() && world.isBlockIndirectlyGettingPowered(x, y, z)) {
					tileEnt.setPowered(true);
				}
				
			}
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		/*
		TileEntity tEnt = (TileEntity) world.getTileEntity(x, y, z);
		if (tEnt != null && tEnt instanceof TileEntityInventoryBase) {
			TileEntityInventoryBase tileEnt = (TileEntityInventoryBase) tEnt;
			Random rand = new Random();
			
			if (tileEnt != null && tileEnt.shouldDropInventory()) {
				for (int j1 = 0; j1 < tileEnt.getSizeInventory(); j1++) {
					ItemStack itemstack = tileEnt.getStackInSlot(j1);
					if (itemstack != null) {
						float f = rand.nextFloat() * 0.8F + 0.1F;
						float f1 = rand.nextFloat() * 0.8F + 0.1F;
						float f2 = rand.nextFloat() * 0.8F + 0.1F;
						EntityItem entityitem;
						
						entityitem = new EntityItem(world, (double) ((float) x + f),
								(double) ((float) y + f1), (double) ((float) z + f2),
								itemstack.copy());
						float f3 = 0.05F;
						entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
						entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
						
						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(
									(NBTTagCompound) itemstack.getTagCompound().copy());
						}
						world.spawnEntityInWorld(entityitem);
					}
				}
				
				world.func_147453_f(x, y, z, block);
			}
		}
		 */
		super.breakBlock(world, x, y, z, block, meta);
		world.removeTileEntity(x, y, z);
	}
	
	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int var1, int var2) {
		super.onBlockEventReceived(world, x, y, z, var1, var2);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity != null ? tileentity.receiveClientEvent(var1, var2) : false;
	}
	
}
