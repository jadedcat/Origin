package com.countrygamer.countrygamercore.Base.client.gui.widget;

import com.countrygamer.countrygamercore.Base.client.gui.GuiScreenBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Component {
	
	protected final GuiScreenBase owner;
	final int column, row;
	
	public Component(GuiScreenBase ownerGui, int col, int row) {
		this.owner = ownerGui;
		this.column = col;
		this.row = row;
		
	}
	
	public int getDisplayColumn() {
		return this.column;
	}
	
	public int getDisplayRow() {
		return this.row;
	}
	
	public abstract void draw(GuiScreenBase gui, int x, int y, int leftOffset, int rightOffset,
			int topOffset, int bottomOffset);
	
	public void onClick(){}
	
}
