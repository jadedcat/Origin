package com.countrygamer.countrygamercore.Base.client.gui.advanced;

import net.minecraft.client.gui.Gui;

import com.countrygamer.countrygamercore.Base.client.gui.GuiScreenBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ScrollBar {
	private static final int SCROLL_WIDTH = 7;
	private static final int SCROLL_BAR_WIDTH = 5;
	private static final int SCROLL_BAR_HEIGHT = 6;
	private static final int SCROLL_BAR_SRC_X = 250;
	private static final int SCROLL_BAR_SRC_Y = 167;
	private int x;
	private int y;
	private int h;
	private int u;
	private int v;
	private int left;
	private int scroll;
	private boolean isScrolling;
	
	public ScrollBar(int x, int y, int h, int u, int v, int left) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.u = u;
		this.v = v;
		this.left = left;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isVisible(Gui gui) {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	protected void onUpdate() {
	}
	
	@SideOnly(Side.CLIENT)
	public void draw(GuiScreenBase gui) {
		if (isVisible(gui)) {
			gui.drawRect(this.x, this.y, this.u, this.v, 7, this.h);
			gui.drawRect(this.x + 1, this.y + 1 + this.scroll, 250, 167, 5, 6);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onClick(GuiScreenBase gui, int mX, int mY) {
		if ((isVisible(gui)) && (gui.inBounds(this.x, this.y, 7, this.h, mX, mY))) {
			this.isScrolling = true;
			updateScroll(mY);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onDrag(GuiScreenBase gui, int mX, int mY) {
		if (isVisible(gui)) updateScroll(mY);
	}
	
	@SideOnly(Side.CLIENT)
	public void onRelease(GuiScreenBase gui, int mX, int mY) {
		if (isVisible(gui)) {
			updateScroll(mY);
			this.isScrolling = false;
		}
	}
	
	private void updateScroll(int mY) {
		if (this.isScrolling) setScroll(mY - this.y - 3);
	}
	
	public int getRawScroll() {
		return this.scroll;
	}
	
	public float getScroll() {
		return this.scroll / (this.h - 6 - 2);
	}
	
	public void resetScroll() {
		this.scroll = 0;
	}
	
	private void setScroll(int newScroll) {
		int old = this.scroll;
		this.scroll = newScroll;
		if (this.scroll < 0)
			this.scroll = 0;
		else if (this.scroll > this.h - 6 - 2) {
			this.scroll = (this.h - 6 - 2);
		}
		if (this.scroll != old) onUpdate();
	}
	
	public void onScroll(GuiScreenBase gui, int mX, int mY, int scroll) {
		if ((isVisible(gui))
				&& (gui.inBounds(this.left, this.y, this.x + 7 - this.left, this.h, mX, mY)))
			setScroll(this.scroll - scroll / 20);
	}
}
