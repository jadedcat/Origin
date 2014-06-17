package com.countrygamer.countrygamercore.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.countrygamer.countrygamercore.Base.common.tile.ICamouflage;
import com.countrygamer.countrygamercore.lib.ItemMeta;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockCamouflageRender implements ISimpleBlockRenderingHandler {
	
	public static final int RENDER_ID = 42025;
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();
		
		/*
		if (flag && this.useInventoryTint) {
			k = block.getRenderColor(metadata);
			f2 = (float) (k >> 16 & 255) / 255.0F;
			f3 = (float) (k >> 8 & 255) / 255.0F;
			float f4 = (float) (k & 255) / 255.0F;
			GL11.glColor4f(f2 * p_147800_3_, f3 * p_147800_3_, f4 * p_147800_3_, 1.0F);
		}
		*/
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();
		
		/*
		if (flag && this.useInventoryTint) {
			GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F);
		}
		*/
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
				renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block,
			int modelId, RenderBlocks renderer) {
		return this.render(world, x, y, z, block, modelId, renderer);
	}
	
	private boolean render(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		if (modelId == this.getRenderId()) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity != null && tileEntity instanceof ICamouflage) {
				ICamouflage camo = (ICamouflage) tileEntity;
				
				if (camo.isCamouflaged()) {
					ItemMeta im = camo.getCamouflage();
					Block camoBlock = Block.getBlockFromItem(im.getItem());
					
					if (camoBlock != null && camoBlock != Blocks.air) {
						renderer.renderBlockAllFaces(camoBlock, x, y, z);
						return true;
					}
				}
			}
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	@Override
	public int getRenderId() {
		return RENDER_ID;
	}
	
}
