package com.countrygamer.core.client.render;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.countrygamer.core.Base.client.Render.TileEntityRendererBase;
import com.countrygamer.core.common.Core;
import com.countrygamer.core.common.craftingsystem.DiagramRecipes;
import com.countrygamer.core.common.tileentity.TileEntityDiagram;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Country_Gamer on 3/19/14.
 */
@SideOnly(Side.CLIENT)
public class TileEntityDiagramRenderer extends TileEntitySpecialRenderer {

	public TileEntityDiagramRenderer () {
	}

	@Override
	public void renderTileEntityAt (TileEntity tileEntity, double d1, double d2, double d3, float f) {
		World world = tileEntity.getWorldObj();
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		world.scheduleBlockUpdate(x, y, z, world.getBlock(x, y, z), 10);

		if (tileEntity instanceof TileEntityDiagram) {
			TileEntityDiagram tileEnt = (TileEntityDiagram)tileEntity;

			DiagramRecipes.Recipe recipe = tileEnt.getRecipe();
			if (recipe == null) {
				Core.log.info("Null recipe");
				return;
			}

			ArrayList<float[]>[] uvsList = tileEnt.getUVsToRender();

			GL11.glPushMatrix();
			//GL11.glTranslated(d1, d2, d3);




			Block renderBlock = recipe.outputBlock;
			int block_meta = recipe.blockMetadata;


			//Disable standard lighting. This is done the same as in TileEntityRendererPiston.
			RenderHelper.disableStandardItemLighting();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);//TODO GL11.GL_ONE_MINUS_SRC_ALPHA);
			//GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(GL11.GL_RED, GL11.GL_GREEN, GL11.GL_BLUE, 0.5F);

			if (renderBlock.hasTileEntity(block_meta)) { // render with custom renderer
				TileEntity renderTileEnt = renderBlock.createTileEntity(world, block_meta);
				TileEntitySpecialRenderer specialRenderer =
						TileEntityRendererDispatcher.instance.getSpecialRenderer(renderTileEnt);
				if (specialRenderer != null) {
					//Core.log.info(specialRenderer.toString());
					if (specialRenderer instanceof TileEntityRendererBase) {
						((TileEntityRendererBase)specialRenderer)
								.renderBasicModelForTile(world, x, y, z, d1, d2, d3, f);
					}
				}
			}
			else { // render as normal block
				//this.renderStandardBlock(tileEnt, d1, d2, d3, f);

				this.bindTexture(TextureMap.locationBlocksTexture);

				//GL11.glDisable(GL11.GL_CULL_FACE); This was initially part of the code that was taken from the piston, but it was causing some of the rendering to look of, e.g. with a cobweb (it would look thicker as both sides were rendering due to this being disabled).

				if (Minecraft.isAmbientOcclusionEnabled())
				{
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}
				else
				{
					GL11.glShadeModel(GL11.GL_FLAT);
				}
				GL11.glPushMatrix();
				GL11.glTranslated(d1, d2, d3);
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				RenderBlocks renderBlocks = new RenderBlocks(tileEntity.getWorldObj());

				//To get the block to render in the right place, I had to pass in 0, 0, 0 as the coordinates for renderBlockByRenderType. This however means that it will use the properties of the block at 0, 0, 0 for some things. This translation allows for the actual block coordinates to be passed in.
				tessellator.setTranslation(-x, -y, -z);
				renderBlocks.renderBlockByRenderType(renderBlock, x, y, z);
				tessellator.draw();
				tessellator.setTranslation(0, 0, 0);
				GL11.glPopMatrix();
			}
			GL11.glDisable(GL11.GL_BLEND);

			//Reset to standard lighting
			RenderHelper.enableStandardItemLighting();

			GL11.glPopMatrix();
		}

	}

	private void renderValidUV(
			TileEntityDiagram tileEnt, int x, int y, int z, double d1, double d2, double d3,
			Tessellator t) {
		//this.renderUV(d1, d2, d3, 0, 1, 0, 1, 'z', 0);
		/*
		ArrayList<float[]>[] uvsList = this.getUVsToRender(tileEnt);
		for (int side = 0; side < uvsList.length; side++) {
			for (float[] uv : uvsList[side]) {
				float minU = uv[0];
				float maxU = uv[1];
				float minV = uv[2];
				float maxV = uv[3];

				String text = "\n" + side + "\n";
				text += minU + ":" + minV + "\n";
				text += minU + ":" + maxV + "\n";
				text += maxU + ":" + minV + "\n";
				text += maxU + ":" + maxV + "\n";
				if (tileEnt.getWorldObj().isRemote)
					Core.log.info(text);
				if (side == 0 || side == 1) {
					//this.renderUV(t, d1, d2, d3, minU, maxU, minV, maxV, 'y', side - 0);
				}
				else if (side == 2 || side == 3) {
					//this.renderUV(t, d1, d2, d3, minU, maxU, minV, maxV, 'z', side - 2);
				}
				else if (side == 4 || side == 5) {
					//this.renderUV(t, d1, d2, d3, minU, maxU, minV, maxV, 'x', side - 4);
				}
			}
		}
		*/
	}

	private void renderUV(
			Tessellator t,
			float minU, float maxU, float minV, float maxV,
			char axis, float offset) {

		/*
		t.addVertexWithUV(0, 0, 0, 0, 0);//bottom left texture
		t.addVertexWithUV(0, 1, 0, 0, 1);//top left
		t.addVertexWithUV(1, 1, 0, 1, 1);//top right
		t.addVertexWithUV(1, 0, 0, 1, 0);//bottom right
		*/
		if (axis == 'x') {
			t.addVertexWithUV(offset, minV, minU, 0, 0);
			t.addVertexWithUV(offset, minV, maxU, 0, 1);
			t.addVertexWithUV(offset, maxV, maxU, 1, 1);
			t.addVertexWithUV(offset, maxV, minU, 1, 0);
		}
		else if (axis == 'y') {
			t.addVertexWithUV(minU, offset, minV, 0, 0);
			t.addVertexWithUV(minU, offset, maxV, 0, 1);
			t.addVertexWithUV(maxU, offset, maxV, 1, 1);
			t.addVertexWithUV(maxU, offset, minV, 1, 0);
		}
		else if (axis == 'z') {
			t.addVertexWithUV(minU, minV, offset, 0, 0);
			t.addVertexWithUV(minU, maxV, offset, 0, 1);
			t.addVertexWithUV(maxU, maxV, offset, 1, 1);
			t.addVertexWithUV(maxU, minV, offset, 1, 0);
		}
	}

}
