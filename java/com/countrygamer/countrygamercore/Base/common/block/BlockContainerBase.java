package com.countrygamer.countrygamercore.Base.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.countrygamercore.Base.common.tile.TileEntityBase;
import com.countrygamer.countrygamercore.Base.common.tile.TileEntityInventoryBase;
import com.countrygamer.countrygamercore.lib.UtilDrops;

/**
 * Basic Block class for tile entity blocks
 * 
 * @author Country_Gamer
 * 
 */
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
		super.breakBlock(world, x, y, z, block, meta);
		world.removeTileEntity(x, y, z);
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityBase) {
			ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
			((TileEntityBase) tileEntity).getTileEntityDrops(drops);
			UtilDrops.spawnDrops(world, x, y, z, drops);
		}
	}
	
	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int var1, int var2) {
		super.onBlockEventReceived(world, x, y, z, var1, var2);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity != null ? tileentity.receiveClientEvent(var1, var2) : false;
	}
	
}
