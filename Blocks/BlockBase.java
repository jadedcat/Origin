package com.countrygamer.countrygamer_core.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Basic block class. Auto registers block, icon, and name
 * 
 * @author Country Gamer
 * 
 */
public class BlockBase extends Block {
	
	public String	modid;
	
	public BlockBase(Material mat, String modid, String name) {
		super(mat);
		//this.setUnlocalizedName(name);
		
		GameRegistry.registerBlock(this, name);
		//LanguageRegistry.addName(this, name);
		
		this.modid = modid.toLowerCase();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
	}
	
}
