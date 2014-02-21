package com.countrygamer.countrygamer_core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamer_core.lib.CoreReference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonArrow extends GuiButton {

	public final GuiButtonArrow.ButtonType buttonType;
	public final int longSide = 15, shortSide = 10;
	
	public static enum ButtonType {
		UP, DOWN, LEFT, RIGHT
	}
	
	public GuiButtonArrow(int id, int x, int y, ButtonType type) {
		super(id, x, y, 0, 0, "");
		this.buttonType = type;
		if (this.buttonType == ButtonType.LEFT
				|| this.buttonType == ButtonType.RIGHT) {
			this.width = this.shortSide;
			this.height = this.longSide;
		}else{
			this.width = this.longSide;
			this.height = this.shortSide;
		}

	}

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.visible) {
			par1Minecraft.getTextureManager().bindTexture(
					new ResourceLocation(CoreReference.MOD_ID,
							"textures/gui/buttons.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = par2 >= this.xPosition && par3 >= this.yPosition
					&& par2 < this.xPosition + this.width
					&& par3 < this.yPosition + this.height;
			int k = 0, l = 0;

			switch (this.buttonType) {
			case UP:
				// 0, 0
				break;
			case DOWN:
				// 16, 0
				l = this.longSide + 1;
				break;
			case LEFT:
				// 0, 11
				k = this.shortSide + 1;
				break;
			case RIGHT:
				// 16, 11
				l = this.longSide + 1;
				k = this.shortSide + 1;
				break;
			}
			
			k += 60;

			this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k,
					this.width, this.height);
		}
	}
}
