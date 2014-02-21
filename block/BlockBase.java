package com.countrygamer.countrygamer_core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
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

	public String modid;

	public BlockBase(Material mat, String modid, String name) {
		super(mat);
		this.setBlockName(name);

		GameRegistry.registerBlock(this, name);
		// LanguageRegistry.addName(this, name);

		this.modid = modid.toLowerCase();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float x1, float y1, float z1) {
		return false;
	}

	public void onEntityWalking(World world, int x,
			int y, int z, Entity entity) {
	}

}
