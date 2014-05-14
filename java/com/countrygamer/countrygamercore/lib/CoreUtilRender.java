package com.countrygamer.countrygamercore.lib;

import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

/**
 * Created by Country_Gamer on 3/21/14.
 */
public class CoreUtilRender {

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

	public static void renderItem(TileEntityInventoryBase tileEnt,
	                        RenderItem itemRender, ItemStack stack, float x, float y, float z) {
		GL11.glPushMatrix();
		float scaleFactor = CoreUtil.getGhostItemScaleFactor(itemRender,
				stack);
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

}
