package com.countrygamer.countrygamercore.Base.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.countrygamer.countrygamercore.Base.client.gui.GuiScreenBase;

public class WidgetTree {
	
	IWidgetOwner owner;
	GuiScreenBase parentScreen;
	final int boxX, boxY, boxW, boxH;
	
	int minColumn, maxColumn, minRow, maxRow;
	int innerBoxLeft, innerBoxRight;
	int innerBoxTop, innerBoxBottom;
	int innerBoxW_Override = -1, innerBoxH_Override = -1;
	
	float scale = 1.0F;
	final ResourceLocation background;
	
	protected double prevMapPosX;
	protected double prevMapPosY;
	protected double mapPosX;
	protected double mapPosY;
	protected double savedMapPosX_maybe;
	protected double savedMapPosY_maybe;
	
	private int isMouseButtonDown;
	protected int mouseX_Saved;
	protected int mouseY_Saved;
	protected float some_float_value = 1.0F;
	
	List<Component> components = new ArrayList<Component>();
	
	public WidgetTree(IWidgetOwner owner, GuiScreenBase parentScreen, int x, int y, int boxWidth,
			int boxHeight, int minCol, int maxCol, int minRow, int maxRow, int startingCol,
			int startingRow, ResourceLocation background) {
		this.owner = owner;
		this.parentScreen = parentScreen;
		this.boxX = x;
		this.boxY = y;
		this.boxW = boxWidth;
		this.boxH = boxHeight;
		
		this.updateBox(this.minColumn = minCol, this.maxColumn = maxCol, this.minRow = minRow,
				this.maxRow = maxRow);
		
		this.background = background;
		
		short offsetX = 141;
		short offsetY = 141;
		this.prevMapPosX = this.mapPosX = this.savedMapPosX_maybe = (double) (startingCol * 24
				- offsetX / 2 - 12);
		this.prevMapPosY = this.mapPosY = this.savedMapPosY_maybe = (double) (startingRow * 24 - offsetY / 2);
		
	}
	
	public void addComponent(Component comp) {
		this.components.add(comp);
	}
	
	public void overrideBoxBounds(int innerBoxWidth, int innerBoxHeight) {
		this.innerBoxW_Override = innerBoxWidth;
		this.innerBoxH_Override = innerBoxHeight;
		this.updateBox(this.minColumn, this.maxColumn, this.minRow, this.maxRow);
	}
	
	public void updateBox(int minCol, int maxCol, int minRow, int maxRow) {
		int bufferWidth = 100;
		this.innerBoxLeft = minCol * 24 - bufferWidth;
		this.innerBoxRight = maxCol * 24 - this.boxW + 24 + bufferWidth;
		
		int bufferHeight = 77;
		this.innerBoxTop = minRow * 24 - bufferHeight;
		this.innerBoxBottom = maxRow * 24 - bufferHeight;
		
	}
	
	public void updateWidget() {
		this.prevMapPosX = this.mapPosX;
		this.prevMapPosY = this.mapPosY;
		double mapX = this.savedMapPosX_maybe - this.mapPosX;
		double mapY = this.savedMapPosY_maybe - this.mapPosY;
		
		if (mapX * mapX + mapY * mapY < 4.0D) {
			this.mapPosX += mapX;
			this.mapPosY += mapY;
		}
		else {
			this.mapPosX += mapX * 0.85D;
			this.mapPosY += mapY * 0.85D;
		}
	}
	
	public void drawWidget(int mouseX, int mouseY, float rpt) {
		if (Mouse.isButtonDown(0)) {
			if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && mouseX >= this.boxX
					&& mouseX < this.boxX + this.boxW && mouseY >= this.boxY
					&& mouseY < this.boxY + this.boxH) {
				if (this.isMouseButtonDown == 0) {
					this.isMouseButtonDown = 1;
				}
				else {
					this.mapPosX -= (double) ((float) (mouseX - this.mouseX_Saved) * this.some_float_value);
					this.mapPosY -= (double) ((float) (mouseY - this.mouseY_Saved) * this.some_float_value);
					this.savedMapPosX_maybe = this.prevMapPosX = this.mapPosX;
					this.savedMapPosY_maybe = this.prevMapPosY = this.mapPosY;
				}
				
				this.mouseX_Saved = mouseX;
				this.mouseY_Saved = mouseY;
			}
		}
		else {
			this.isMouseButtonDown = 0;
		}
		/*
		int dWheel = Mouse.getDWheel();
		float f4 = this.some_float_value;
		
		if (dWheel < 0) {
			this.some_float_value += 0.25F;
		}
		else if (dWheel > 0) {
			this.some_float_value -= 0.25F;
		}
		
		this.some_float_value = MathHelper.clamp_float(this.some_float_value, 1.0F, 2.0F);
		
		if (this.some_float_value != f4) {
			int xSize = this.owner.getXSize();
			int ySize = this.owner.getYSize();
			float f5 = f4 * (float) xSize;
			float f1 = f4 * (float) ySize;
			float f2 = this.some_float_value * (float) xSize;
			float f3 = this.some_float_value * (float) ySize;
			this.mapPosX -= (double) ((f2 - f5) * 0.5F);
			this.mapPosY -= (double) ((f3 - f1) * 0.5F);
			this.savedMapPosX_maybe = this.prevMapPosX = this.mapPosX;
			this.savedMapPosY_maybe = this.prevMapPosY = this.mapPosY;
		}
		
		this.savedMapPosX_maybe = this.getWithin(this.savedMapPosX_maybe,
				(double) this.innerBoxLeft, (double) this.innerBoxRight);
		this.savedMapPosY_maybe = this.getWithin(this.savedMapPosY_maybe,
				(double) this.innerBoxTop, (double) this.innerBoxBottom);
		 */
		this.drawComponents(mouseX, mouseY, rpt);
	}
	
	protected void drawComponents(int mouseX, int mouseY, float rpt) {
		int currentMapPosX = MathHelper.floor_double(this.prevMapPosX
				+ (this.mapPosX - this.prevMapPosX) * (double) rpt);
		int currentMapPosY = MathHelper.floor_double(this.prevMapPosY
				+ (this.mapPosY - this.prevMapPosY) * (double) rpt);
		
		currentMapPosX = this.getWithin(currentMapPosX, this.innerBoxLeft, this.innerBoxRight);
		currentMapPosY = this.getWithin(currentMapPosY, this.innerBoxTop, this.innerBoxBottom);
		
		if (currentMapPosX >= this.innerBoxRight) {
			currentMapPosX = (int) (this.innerBoxRight) - 1;
		}
		
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) this.boxX, (float) this.boxY, 0.0F);
		GL11.glScalef(1.0F / this.some_float_value, 1.0F / this.some_float_value, 0.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glPushMatrix();
		this.parentScreen.mc.getTextureManager().bindTexture(this.background);
		
		// int texWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0,
		// GL11.GL_TEXTURE_WIDTH);
		// int texHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0,
		// GL11.GL_TEXTURE_HEIGHT);
		
		float scaleW = 1.0F;
		float scaleH = 1.0F;
		
		this.parentScreen.drawTexturedModalRect(0, 0, 0, 0, this.boxW, this.boxH);
		
		GL11.glPopMatrix();
		
		int index;
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int i5;
		int j5;
		
		for (index = 0; index < this.components.size(); ++index) {
			Component comp = this.components.get(index);
			i5 = comp.getDisplayColumn() * 24 - currentMapPosX;
			j5 = comp.getDisplayRow() * 24 - currentMapPosY;
			
			if (i5 >= -24 && j5 >= -24 && (float) i5 <= (float) this.boxW
					&& (float) j5 <= (float) this.boxH) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				comp.draw(this.parentScreen, i5 - 2, j5 - 2);
				
			}
		}
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	private int getWithin(int pos, int min, int max) {
		return (int) this.getWithin((double) pos, (double) min, (double) max);
	}
	
	private double getWithin(double pos, double min, double max) {
		if (pos < min) {
			return min;
		}
		
		if (pos >= max) {
			return (max - 1);
		}
		
		return pos;
	}
	
}
