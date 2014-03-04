package com.countrygamer.core.block;

import com.countrygamer.core.block.tiles.TileEntityInventoryBase;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
		this.blockIcon = iconRegister.registerIcon(this.modid
				+ ":"
				+ this.getUnlocalizedName().substring(
						this.getUnlocalizedName().indexOf(".") + 1));
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
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float x1, float y1, float z1) {
		return false;
	}
	
}
