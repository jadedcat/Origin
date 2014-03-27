package com.countrygamer.core.Base.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.core.Base.block.tiles.TileEntityBase;
import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockContainerBase extends BlockContainer {
	
	public String modid;
	public Class<? extends TileEntity> tileEntityClass;
	
	public BlockContainerBase(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat);
		this.setBlockName(name);
		GameRegistry.registerBlock(this, name);
		this.tileEntityClass = tileEntityClass;
		
		this.modid = modid;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":"
				+ this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
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
	
}
