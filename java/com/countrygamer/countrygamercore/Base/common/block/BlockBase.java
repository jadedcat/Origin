package com.countrygamer.countrygamercore.base.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Basic block class. Auto registers block, icon, and name.
 * 
 * @author Country_Gamer
 * 
 */
public class BlockBase extends Block {
	
	public String modid;
	
	public BlockBase(Material mat, String modid, String name) {
		this(mat, modid, name, null);
	}
	
	public BlockBase(Material mat, String modid, String name, Class<? extends ItemBlock> item) {
		super(mat);
		this.setBlockName(name);
		
		if (item == null)
			GameRegistry.registerBlock(this, name);
		else
			GameRegistry.registerBlock(this, item, name);
		
		this.modid = modid.toLowerCase();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":" + this.getName());
	}
	
	public String getName() {
		return this.getUnlocalizedName().substring(5);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		return false;
	}
	
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
	}
	
	public boolean hasTileEntityDrops() {
		return false;
	}
	
	/*
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		// Core.logger.info("Break Block");
		if (this.hasTileEntityDrops()) {
			ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity != null && tileEntity instanceof TileEntityBase)
				((TileEntityBase) tileEntity).getTileEntityDrops(ret);
			for (final ItemStack stack : ret) {
				if (!world.isRemote) CoreUtil.dropItemStack(world, stack, x, y, z);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	 */
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		// Core.logger.info("Drops");
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		if (!this.hasTileEntityDrops())
			ret.addAll(super.getDrops(world, x, y, z, metadata, fortune));
		
		return ret;
	}
	
}
