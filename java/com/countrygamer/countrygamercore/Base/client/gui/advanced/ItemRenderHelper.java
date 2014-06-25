package com.countrygamer.countrygamercore.base.client.gui.advanced;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRenderHelper {
	private static RenderBlocks renderBlocks = new RenderBlocks();
	
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	
	public static void renderItemIntoGUI(RenderItem renderItem, TextureManager textureManager,
			ItemStack itemStack, int x, int y, boolean renderEffect) {
		if ((itemStack == null)
				|| (ForgeHooksClient.renderInventoryItem(renderBlocks, textureManager, itemStack,
						renderItem.renderWithColor, renderItem.zLevel, x, y))) {
			return;
		}
		
		// int k = itemStack.itemID;
		Item item = itemStack.getItem();
		int l = itemStack.getItemDamage();
		Object object = itemStack.getIconIndex();
		
		Block block = Block.getBlockFromItem(item);// k < Block.blocksList.length ?
													// Block.blocksList[k] : null;
		if ((itemStack.getItemSpriteNumber() == 0) && (block != null)
				&& (RenderBlocks.renderItemIn3d(block.getRenderType()))) {
			textureManager.bindTexture(TextureMap.locationBlocksTexture);
			GL11.glPushMatrix();
			GL11.glTranslatef(x - 2, y + 3, -3.0F + renderItem.zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int i1 = item.getColorFromItemStack(itemStack, 0);
			float f = (i1 >> 16 & 0xFF) / 255.0F;
			float f1 = (i1 >> 8 & 0xFF) / 255.0F;
			float f2 = (i1 & 0xFF) / 255.0F;
			
			if (renderItem.renderWithColor) {
				GL11.glColor4f(f, f1, f2, 1.0F);
			}
			
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			renderBlocks.useInventoryTint = renderItem.renderWithColor;
			renderBlocks.renderBlockAsItem(block, l, 1.0F);
			renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
		}
		else if (item.requiresMultipleRenderPasses()) {
			GL11.glDisable(2896);
			
			for (int j1 = 0; j1 < item.getRenderPasses(l); j1++) {
				textureManager
						.bindTexture(itemStack.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture
								: TextureMap.locationItemsTexture);
				IIcon icon = item.getIcon(itemStack, j1);
				int k1 = item.getColorFromItemStack(itemStack, j1);
				float f1 = (k1 >> 16 & 0xFF) / 255.0F;
				float f2 = (k1 >> 8 & 0xFF) / 255.0F;
				float f3 = (k1 & 0xFF) / 255.0F;
				
				if (renderItem.renderWithColor) {
					GL11.glColor4f(f1, f2, f3, 1.0F);
				}
				
				renderItem.renderIcon(x, y, icon, 16, 16);
				
				if ((!renderEffect) || (!itemStack.hasEffect(j1))) continue;
				renderEffect(renderItem, textureManager, x, y);
			}
			
			GL11.glEnable(2896);
		}
		else {
			GL11.glDisable(2896);
			ResourceLocation resourcelocation = textureManager.getResourceLocation(itemStack
					.getItemSpriteNumber());
			textureManager.bindTexture(resourcelocation);
			
			if (object == null) {
				object = ((TextureMap) Minecraft.getMinecraft().getTextureManager()
						.getTexture(resourcelocation)).getAtlasSprite("missingno");
			}
			
			int i1 = item.getColorFromItemStack(itemStack, 0);
			float f = (i1 >> 16 & 0xFF) / 255.0F;
			float f1 = (i1 >> 8 & 0xFF) / 255.0F;
			float f2 = (i1 & 0xFF) / 255.0F;
			
			if (renderItem.renderWithColor) {
				GL11.glColor4f(f, f1, f2, 1.0F);
			}
			
			renderItem.renderIcon(x, y, (IIcon) object, 16, 16);
			GL11.glEnable(2896);
			
			if ((renderEffect) && (itemStack.hasEffect(0))) {
				renderEffect(renderItem, textureManager, x, y);
			}
		}
		
		GL11.glEnable(2884);
	}
	
	private static void renderEffect(RenderItem renderItem, TextureManager manager, int x, int y) {
		GL11.glDepthFunc(514);
		GL11.glDisable(2896);
		GL11.glDepthMask(false);
		manager.bindTexture(RES_ITEM_GLINT);
		GL11.glEnable(3042);
		GL11.glBlendFunc(774, 774);
		GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
		renderGlint(renderItem, x - 2, y - 2, 20, 20);
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glEnable(2896);
		GL11.glDepthFunc(515);
	}
	
	private static void renderGlint(RenderItem renderItem, int par2, int par3, int par4, int par5) {
		for (int j1 = 0; j1 < 2; j1++) {
			if (j1 == 0) {
				GL11.glBlendFunc(768, 1);
			}
			
			if (j1 == 1) {
				GL11.glBlendFunc(768, 1);
			}
			
			float f = 0.0039063F;
			float f1 = 0.0039063F;
			float f2 = (float) (Minecraft.getSystemTime() % (3000 + j1 * 1873))
					/ (3000.0F + j1 * 1873) * 256.0F;
			float f3 = 0.0F;
			Tessellator tessellator = Tessellator.instance;
			float f4 = 4.0F;
			
			if (j1 == 1) {
				f4 = -1.0F;
			}
			
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(par2 + 0, par3 + par5, renderItem.zLevel, (f2 + par5 * f4)
					* f, (f3 + par5) * f1);
			tessellator.addVertexWithUV(par2 + par4, par3 + par5, renderItem.zLevel,
					(f2 + par4 + par5 * f4) * f, (f3 + par5) * f1);
			tessellator.addVertexWithUV(par2 + par4, par3 + 0, renderItem.zLevel, (f2 + par4) * f,
					(f3 + 0.0F) * f1);
			tessellator.addVertexWithUV(par2 + 0, par3 + 0, renderItem.zLevel, (f2 + 0.0F) * f,
					(f3 + 0.0F) * f1);
			tessellator.draw();
		}
	}
}
