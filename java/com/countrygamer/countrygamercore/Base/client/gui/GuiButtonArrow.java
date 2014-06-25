package com.countrygamer.countrygamercore.base.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.common.Core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A button class for arrows, can come in UP, DOWN, LEFT or RIGHT
 * 
 * @author Country_Gamer
 * 
 */
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
		if (this.buttonType == ButtonType.LEFT || this.buttonType == ButtonType.RIGHT) {
			this.width = this.shortSide;
			this.height = this.longSide;
		}
		else {
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
					new ResourceLocation(Core.pluginID, "textures/gui/buttons.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = par2 >= this.xPosition && par3 >= this.yPosition
					&& par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			
			int u = 0, v = 60;
			
			// 0,00 UP
			// 0,10 DOWN
			// 0,19 LEFT
			// 0,33 RIGHT
			if (this.buttonType == ButtonType.UP) v += 0;
			if (this.buttonType == ButtonType.DOWN) v += 10;
			if (this.buttonType == ButtonType.LEFT) v += 20;
			if (this.buttonType == ButtonType.RIGHT) v += 35;
			
			// int offset = this.getHoverState(flag);
			// 0 disabled
			// 1 not hover
			// 2 hover
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					u + (this.getHoverState(flag) * 15), v, this.width, this.height);
		}
	}
	
}
