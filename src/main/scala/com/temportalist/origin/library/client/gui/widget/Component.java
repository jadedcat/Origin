package com.temportalist.origin.library.client.gui.widget;

import com.temportalist.origin.wrapper.client.gui.GuiScreenWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class Component {

	protected final GuiScreenWrapper owner;
	final int column, row;

	public Component(GuiScreenWrapper ownerGui, int col, int row) {
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

	public abstract void draw(GuiScreenWrapper gui, int x, int y, int leftOffset, int rightOffset,
			int topOffset, int bottomOffset);

	public void onClick() {
	}

	public void onHover(List<String> hoverInfo) {
	}

}
