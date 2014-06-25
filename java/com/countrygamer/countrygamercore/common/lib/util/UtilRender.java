package com.countrygamer.countrygamercore.common.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.base.common.tile.TileEntityInventoryBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Country_Gamer on 3/21/14.
 */

@SideOnly(Side.CLIENT)
public class UtilRender {
	
	public static RenderItem basicRender = new RenderItem() {
		@Override
		public byte getMiniBlockCount(ItemStack stack, byte original) {
			return 1;
		}
		
		@Override
		public byte getMiniItemCount(ItemStack stack, byte original) {
			return 1;
		}
		
		@Override
		public boolean shouldBob() {
			return true;
		}
	};
	
	public static void renderItem(TileEntityInventoryBase tileEnt, RenderItem itemRender,
			ItemStack stack, float x, float y, float z) {
		GL11.glPushMatrix();
		float scaleFactor = UtilRender.getGhostItemScaleFactor(itemRender, stack);
		float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
		
		EntityItem ghostEntityItem = new EntityItem(tileEnt.getWorldObj());
		ghostEntityItem.hoverStart = 0.0F;
		ghostEntityItem.setEntityItemStack(stack);
		
		GL11.glTranslatef(x, y, z);
		GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
		GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
		
		itemRender.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
		GL11.glPopMatrix();
	}
	
	/**
	 * Used for rendering items in block renders
	 * 
	 * @param itemRender
	 * @param itemStack
	 * @return
	 */
	public static float getGhostItemScaleFactor(RenderItem itemRender, ItemStack itemStack) {
		float scaleFactor = 1.0F;
		
		if (itemStack != null) {
			if (itemStack.getItem() instanceof ItemBlock) {
				switch (itemRender.getMiniBlockCount(itemStack, (byte) 0)) {
					case 1:
						return 0.90F;
					case 2:
						return 0.90F;
					case 3:
						return 0.90F;
					case 4:
						return 0.90F;
					case 5:
						return 0.80F;
					default:
						return 0.90F;
				}
			}
			else {
				switch (itemRender.getMiniItemCount(itemStack, (byte) 0)) {
					case 1:
						return 0.65F;
					case 2:
						return 0.65F;
					case 3:
						return 0.65F;
					case 4:
						return 0.65F;
					default:
						return 0.65F;
				}
			}
		}
		
		return scaleFactor;
	}
	
	public static ResourceLocation getResource(String pluginID, String folder, String imgName) {
		return new ResourceLocation(pluginID, "textures/" + folder + imgName + ".png");
	}
	
	public static void bindResource(ResourceLocation rl) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(rl);
	}
	
	public static void bindResource(String pluginID, String folder, String imgName) {
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(getResource(pluginID, folder, imgName));
	}
	
	public static void drawTextureWithOffsets(Gui gui, int x, int y, int u, int v, int w, int h,
			int leftOffset, int rightOffset, int topOffset, int bottomOffset) {
		gui.drawTexturedModalRect(x + leftOffset - rightOffset, y + topOffset, u + leftOffset
				- rightOffset, v + topOffset, w - leftOffset, h - topOffset - bottomOffset);
	}
	
}
