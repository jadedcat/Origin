package com.countrygamer.countrygamercore.base.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.common.Core;
import com.countrygamer.countrygamercore.common.lib.RedstoneState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A new button which can detail one of three {@link RedstoneState}s.
 * 
 * @author Country_Gamer
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiButtonRedstone extends GuiButton {
	
	private RedstoneState redstoneState;
	public boolean isActive = false;
	private boolean useOldStyle;
	
	public GuiButtonRedstone(int id, int x, int y, RedstoneState state) {
		this(id, x, y, state, false);
	}
	
	public GuiButtonRedstone(int id, int x, int y, RedstoneState state, boolean useOld) {
		super(id, x, y, 18, 18, "");
		this.redstoneState = state;
		this.useOldStyle = useOld;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (!this.enabled) this.isActive = false;
		if (this.visible) {
			minecraft.getTextureManager().bindTexture(
					new ResourceLocation(Core.pluginID, "textures/gui/buttons.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			int u = 100;
			int v = 0;
			
			int redstoneStateAsInt = RedstoneState.getIntFromState(this.redstoneState) + 1;
			u += redstoneStateAsInt * 18;
			
			if (this.useOldStyle) {
				u += 54;
			}
			
			boolean flag = x >= this.xPosition && y >= this.yPosition
					&& x < this.xPosition + this.width && y < this.yPosition + this.height;
			int hoverState = this.getHoverState(flag);
			v += hoverState * 18;
			
			if (this.isActive) v += 36;
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width,
					this.height);
		}
	}
	
	
	public RedstoneState getRedstoneState() {
		return this.redstoneState;
	}
	
	public void nextRedstoneState() {
		this.redstoneState = RedstoneState.getStateFromInt(RedstoneState
				.getIntFromState(this.redstoneState) + 1);
	}
	
}
