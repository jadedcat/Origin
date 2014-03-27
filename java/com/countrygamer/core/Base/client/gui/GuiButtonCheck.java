package com.countrygamer.core.Base.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.core.lib.CoreReference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonCheck extends GuiButton {

	private boolean isCheckmark = true;

	public GuiButtonCheck(int id, int x, int y, boolean isCheckmark) {
		super(id, x, y, 20, 20, "");
		this.isCheckmark = isCheckmark;
	}

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft par1Minecraft, int x, int y) {
		if (this.visible) {
			par1Minecraft.getTextureManager().bindTexture(
					new ResourceLocation(CoreReference.MOD_ID,
							"textures/gui/buttons.png"));

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = x >= this.xPosition && y >= this.yPosition
					&& x < this.xPosition + this.width
					&& y < this.yPosition + this.height;
			int k = this.getHoverState(flag);

			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					20 + (this.isCheckmark ? 0 : 20), k*20, this.width,
					this.height);
		}
	}

}
