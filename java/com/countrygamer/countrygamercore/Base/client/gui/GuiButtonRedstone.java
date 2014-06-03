package com.countrygamer.countrygamercore.Base.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.common.Core;
import com.countrygamer.countrygamercore.lib.RedstoneState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A new button which can detail one of three {@link RedstoneState}s. 
 * @author Country_Gamer
 *
 */
@SideOnly(Side.CLIENT)
public class GuiButtonRedstone extends GuiButton {
	
	private final RedstoneState redstoneState;
	public boolean isActive = false;
	
	public GuiButtonRedstone(int id, int x, int y, RedstoneState state) {
		super(id, x, y, 18, 18, "");
		this.redstoneState = state;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (!this.enabled) this.isActive = false;
		if (this.visible) {
			minecraft.getTextureManager().bindTexture(
					new ResourceLocation(Core.pluginID,
							"textures/gui/buttons.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			int u = 100;
			int v = 0;
			
			int redstoneStateAsInt = this.getRedstoneState();
			u += redstoneStateAsInt * 18;
			
			boolean flag = x >= this.xPosition && y >= this.yPosition
					&& x < this.xPosition + this.width
					&& y < this.yPosition + this.height;
			int hoverState = this.getHoverState(flag);
			v += hoverState * 18;
			
			if (this.isActive) v += 36;
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v,
					this.width, this.height);
		}
	}
	
	/**
	 * Returns an integer based on the state of the button.
	 * Returns 0 if there is no state, 1 if redstone is ignored,
	 * 2 if needs no redstone (Low), and 3 if needs redstone (High)
	 * 
	 * @return
	 */
	private int getRedstoneState() {
		if (this.redstoneState == null)
			return 0;
		else if (this.redstoneState == RedstoneState.IGNORE)
			return 1;
		else if (this.redstoneState == RedstoneState.LOW)
			return 2;
		else if (this.redstoneState == RedstoneState.HIGH)
			return 3;
		else
			return -1;
	}
	
}
