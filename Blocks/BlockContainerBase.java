package com.countrygamer.countrygamer_core.Blocks;

import com.countrygamer.countrygamer_core.Blocks.TileEntityInventoryBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockContainerBase extends BlockContainer {
	
	public String	modid;
	
	public BlockContainerBase(Material mat, String modid, String name) {
		super(mat);
		this.setBlockName(name);
		GameRegistry.registerBlock(this, name);
		//LanguageRegistry.addName(this, name);
		
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
		return new TileEntityInventoryBase("", 0, 0);
	}
	
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	
	
}
