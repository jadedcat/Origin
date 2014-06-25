package com.countrygamer.countrygamercore.base.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.base.client.gui.advanced.ItemRenderHelper;
import com.countrygamer.countrygamercore.base.client.gui.advanced.RenderRotation;
import com.countrygamer.countrygamercore.base.client.gui.widget.IWidgetOwner;
import com.countrygamer.countrygamercore.common.Core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Used as a base class for all gui's without an inventory
 * 
 * @author Country_Gamer
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiScreenBase extends GuiScreen implements IWidgetOwner {
	
	protected final int grayTextColor = 4210752;
	
	protected int xSize = 176;
	protected int ySize = 166;
	protected int guiLeft;
	protected int guiTop;
	
	private String title = "";
	private ResourceLocation bkgdTex = null;
	
	private List<GuiTextField> textFieldList = new ArrayList<GuiTextField>();
	
	public GuiScreenBase() {
		this("");
	}
	
	public GuiScreenBase(String title) {
		this(title, new ResourceLocation(Core.pluginID, "textures/gui/blank.png"));
	}
	
	public GuiScreenBase(String title, ResourceLocation background) {
		super();
		this.mc = Minecraft.getMinecraft();
		this.setupGui(title, background);
	}
	
	protected void setupGui(String title, ResourceLocation backgroundTexture) {
		this.title = title;
		this.bkgdTex = backgroundTexture;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		Keyboard.enableRepeatEvents(true);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton gB) {
		int id = gB.id;
		this.buttonPress(id);
	}
	
	protected void buttonPress(int id) {
	}
	
	protected void setupTextField(GuiTextField textField, int maxStrLength) {
		textField.setTextColor(-1);
		textField.setDisabledTextColour(-1);
		textField.setEnableBackgroundDrawing(true);
		textField.setMaxStringLength(maxStrLength);
		textField.setText("");
		this.textFieldList.add(textField);
	}
	
	@Override
	protected void keyTyped(char letter, int par2) {
		boolean wasField = false;
		for (GuiTextField field : this.textFieldList) {
			if (field.textboxKeyTyped(letter, par2)) {
				this.sendKeyPacket(field);
				wasField = true;
			}
		}
		if (!wasField) {
			super.keyTyped(letter, par2);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		for (GuiTextField field : this.textFieldList) {
			field.mouseClicked(x, y, button);
		}
	}
	
	private void sendKeyPacket(GuiTextField txtField) {
		PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
		try {
			packetbuffer.writeStringToBuffer(txtField.getText());
			this.mc.getNetHandler().addToSendQueue(
					new C17PacketCustomPayload("MC|ItemName", packetbuffer));
		} catch (Exception exception) {
			LogManager.getLogger().error("Couldn\'t send command block info", exception);
		} finally {
			packetbuffer.release();
		}
	}
	
	@Override
	public void updateScreen() {
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float renderPartialTicks) {
		this.drawGuiContainerBackgroundLayer(renderPartialTicks, par1, par2);
		this.drawGuiContainerForegroundLayer(par1, par2);
		
		super.drawScreen(par1, par2, renderPartialTicks);
		
		this.drawHoverInfo(par1, par2);
		
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		if (!this.title.equals("")) {
			this.drawTitle(
					this.guiLeft + (this.xSize / 2)
							- (this.fontRendererObj.getStringWidth(this.title) / 2),
					this.guiTop + 5);
		}
		this.foregroundText();
		
	}
	
	protected void drawTitle(int titleX, int titleY) {
		this.fontRendererObj.drawString(this.title, titleX, titleY, this.grayTextColor);
	}
	
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawBackground(i, j, f);
		
		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		
		this.backgroundObjects();
		
	}
	
	protected ResourceLocation getBackground() {
		return this.bkgdTex;
	}
	
	protected void drawBackground(int mouseX, int mouseY, float renderPartialTicks) {
		this.mc.getTextureManager().bindTexture(this.getBackground());
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0,
				this.xSize, this.ySize);
	}
	
	protected void foregroundText() {
		
	}
	
	protected void backgroundObjects() {
	}
	
	protected void string(String str, int x, int y) {
		this.string(str, x, y, this.grayTextColor);
	}
	
	protected void string(String str, int x, int y, int color) {
		this.fontRendererObj.drawString(str, x, y, color);
	}
	
	private void drawHoverInfo(int mouseX, int mouseY) {
		List<String> hoverInfo = new ArrayList<String>();
		
		this.addHoverInformation(mouseX, mouseY, hoverInfo);
		
		if (!hoverInfo.isEmpty()) this.renderHoverTip(hoverInfo, mouseX, mouseY);
	}
	
	public void addHoverInformation(int mouseX, int mouseY, List<String> currentInfomation) {
	}
	
	/**
	 * GuiContainer func_146978_c
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param mX
	 * @param mY
	 * @return
	 */
	public boolean isMouseOver(int x, int y, int w, int h, int mX, int mY) {
		return mX >= x && mX < x + w && mY >= y && mY < y + h;
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	protected void renderHoverTip(List hoverInfo, int mouseX, int mouseY) {
		for (int k = 0; k < hoverInfo.size(); ++k) {
			hoverInfo.set(k, EnumChatFormatting.GRAY + (String) hoverInfo.get(k));
		}
		
		this.func_146283_a(hoverInfo, mouseX, mouseY);
		drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj);
	}
	
	public int getXSize() {
		return this.xSize;
	}
	
	public int getYSize() {
		return this.ySize;
	}
	
	public int getGuiLeft() {
		return this.guiLeft = (this.width - this.xSize) / 2;
	}
	
	public int getGuiTop() {
		return this.guiTop = (this.height - this.ySize) / 2;
	}
	
	// ~~~~~~~~~~~ Start Cut Code! ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// The following code is creditted to Vswe, Lorddusk, and Newcastlegeek ~~~~~~~~
	
	public static final ResourceLocation MAP_TEXTURE = new ResourceLocation(Core.pluginID,
			"textures/gui/advanced/questmap.png");
	protected static final ResourceLocation TERRAIN = new ResourceLocation(
			"textures/atlas/blocks.png");
	
	protected static RenderItem itemRenderer = new RenderItem();
	protected static final int ITEM_SRC_Y = 235;
	protected static final int ITEM_SIZE = 18;
	
	public void drawRect(int x, int y, int u, int v, int w, int h) {
		drawRect(x, y, u, v, w, h, RenderRotation.NORMAL);
	}
	
	public void drawRect(int x, int y, int u, int v, int w, int h, RenderRotation rotation) {
		boolean rotate = (rotation == RenderRotation.ROTATE_90)
				|| (rotation == RenderRotation.ROTATE_270)
				|| (rotation == RenderRotation.ROTATE_90_FLIP)
				|| (rotation == RenderRotation.ROTATE_270_FLIP);
		
		int targetW = rotate ? h : w;
		int targetH = rotate ? w : h;
		
		x += this.guiLeft;
		y += this.guiTop;
		
		float fw = 0.0039063F;
		float fy = 0.0039063F;
		
		double a = (u + 0) * fw;
		double b = (u + w) * fw;
		double c = (v + h) * fy;
		double d = (v + 0) * fy;
		
		double[] ptA = {
				a, c
		};
		double[] ptB = {
				b, c
		};
		double[] ptC = {
				b, d
		};
		double[] ptD = {
				a, d
		};
		double[] pt1;
		double[] pt2;
		double[] pt3;
		double[] pt4;
		switch (rotation.ordinal()) {
			case 1:
			default:
				pt1 = ptA;
				pt2 = ptB;
				pt3 = ptC;
				pt4 = ptD;
				break;
			case 2:
				pt1 = ptB;
				pt2 = ptC;
				pt3 = ptD;
				pt4 = ptA;
				break;
			case 3:
				pt1 = ptC;
				pt2 = ptD;
				pt3 = ptA;
				pt4 = ptB;
				break;
			case 4:
				pt1 = ptD;
				pt2 = ptA;
				pt3 = ptB;
				pt4 = ptC;
				break;
			case 5:
				pt1 = ptB;
				pt2 = ptA;
				pt3 = ptD;
				pt4 = ptC;
				break;
			case 6:
				pt1 = ptA;
				pt2 = ptD;
				pt3 = ptC;
				pt4 = ptB;
				break;
			case 7:
				pt1 = ptD;
				pt2 = ptC;
				pt3 = ptB;
				pt4 = ptA;
				break;
			case 8:
				pt1 = ptC;
				pt2 = ptB;
				pt3 = ptA;
				pt4 = ptD;
		}
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + targetH, this.zLevel, pt1[0], pt1[1]);
		tessellator.addVertexWithUV(x + targetW, y + targetH, this.zLevel, pt2[0], pt2[1]);
		tessellator.addVertexWithUV(x + targetW, y + 0, this.zLevel, pt3[0], pt3[1]);
		tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, pt4[0], pt4[1]);
		tessellator.draw();
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public void drawMouseOver(String str, int x, int y) {
		List lst = new ArrayList();
		String[] split = str.split("\n");
		for (String s : split) {
			lst.add(s);
		}
		drawMouseOver(lst, x, y);
	}
	
	public void drawMouseOver(List<String> str, int x, int y) {
		GL11.glDisable(2929);
		
		int w = 0;
		
		for (String line : str) {
			int l = this.fontRendererObj.getStringWidth(line);
			
			if (l > w) {
				w = l;
			}
		}
		
		x += 12;
		y -= 12;
		int h = 8;
		
		if (str.size() > 1) {
			h += 2 + (str.size() - 1) * 10;
		}
		
		if (x + w > this.width) {
			x -= 28 + w;
		}
		
		if (y + h + 6 > this.height) {
			y = this.height - h - 6;
		}
		
		this.zLevel = 300.0F;
		int bg = -267386864;
		drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, bg, bg);
		drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, bg, bg);
		drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, bg, bg);
		drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, bg, bg);
		drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, bg, bg);
		int border1 = 1347420415;
		int border2 = (border1 & 0xFEFEFE) >> 1 | border1 & 0xFF000000;
		drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, border1, border2);
		drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, border1, border2);
		drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, border1, border1);
		drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, border2, border2);
		
		for (int i = 0; i < str.size(); i++) {
			String line = (String) str.get(i);
			this.fontRendererObj.drawStringWithShadow(line, x, y, -1);
			
			if (i == 0) {
				y += 2;
			}
			
			y += 10;
		}
		
		this.zLevel = 0.0F;
		GL11.glEnable(2929);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, int thickness, int color) {
		GL11.glDisable(3553);
		applyColor(color);
		
		GL11.glEnable(2848);
		GL11.glLineWidth(1.0F + thickness * this.width / 500.0F);
		
		GL11.glBegin(1);
		GL11.glVertex3f(x1, y1, 0.0F);
		GL11.glVertex3f(x2, y2, 0.0F);
		GL11.glEnd();
		
		GL11.glEnable(3553);
	}
	
	public void applyColor(int color) {
		float a = (color >> 24 & 0xFF) / 255.0F;
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		
		GL11.glColor4f(r, g, b, a);
	}
	
	public void drawIcon(IIcon icon, int x, int y) {
		this.drawTexturedModelRectFromIcon(this.guiLeft + x, this.guiTop + y, icon, 16, 16);
	}
	
	public void drawFluid(Fluid fluid, int x, int y, int mX, int mY) {
		drawItemBackground(x, y, mX, mY, false);
		if (fluid != null) drawFluid(fluid, x + 1, y + 1);
	}
	
	public void drawFluid(Fluid fluid, int x, int y) {
		IIcon icon = fluid.getIcon();
		
		if (icon == null) {
			if (FluidRegistry.WATER.equals(fluid))
				icon = Blocks.water.getIcon(0, 0);
			else if (FluidRegistry.LAVA.equals(fluid)) {
				icon = Blocks.lava.getIcon(0, 0);
			}
		}
		
		if (icon != null) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(MAP_TEXTURE);
			drawRect(x, y, 240, 240, 16, 16);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(TERRAIN);
			setColor(fluid.getColor());
			drawIcon(icon, x, y);
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
	protected void drawItemBackground(int x, int y, int mX, int mY, boolean selected) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(MAP_TEXTURE);
		drawRect(x, y, inBounds(x, y, 18, 18, mX, mY) ? 18 : 0, 235, 18, 18);
		if (selected) drawRect(x, y, 36, 235, 18, 18);
	}
	
	public void drawItem(ItemStack item, int x, int y, int mX, int mY, boolean selected) {
		drawItemBackground(x, y, mX, mY, selected);
		
		if ((item != null) && (item.getItem() != null)) {
			drawItem(item, x + 1, y + 1, true);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, Minecraft.getMinecraft()
					.getTextureManager(), item, x + this.guiLeft + 1, y + this.guiTop + 1);
		}
		GL11.glDisable(2896);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}
	
	protected void setColor(int color) {
		float[] colorComponents = new float[3];
		for (int i = 0; i < colorComponents.length; i++) {
			colorComponents[i] = (((color & 255 << i * 8) >> i * 8) / 255.0F);
		}
		GL11.glColor4f(colorComponents[2], colorComponents[1], colorComponents[0], 1.0F);
	}
	
	public void drawItem(ItemStack itemstack, int x, int y, boolean renderEffect) {
		if ((itemstack == null) || (itemstack.getItem() == null)) return;
		
		GL11.glPushMatrix();
		
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(2896);
		GL11.glEnable(32826);
		GL11.glEnable(2903);
		GL11.glEnable(2896);
		
		itemRenderer.zLevel = 1.0F;
		try {
			ItemRenderHelper.renderItemIntoGUI(itemRenderer, this.mc.getTextureManager(),
					itemstack, x + this.guiLeft, y + this.guiTop, renderEffect);
		} finally {
			itemRenderer.zLevel = 0.0F;
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(2896);
			
			GL11.glPopMatrix();
		}
	}
	
	public float getZLevel() {
		return this.zLevel;
	}
	
	public void setZLevel(float zLevel) {
		this.zLevel = zLevel;
	}
	
	public int getLeft() {
		return this.guiLeft;
	}
	
	public int getTop() {
		return this.guiTop;
	}
	
	public int getStringWidth(String txt) {
		return this.fontRendererObj.getStringWidth(txt);
	}
	
	public void drawString(String str, int x, int y, int color) {
		drawString(str, x, y, 1.0F, color);
	}
	
	public void drawString(String str, int x, int y, float mult, int color) {
		GL11.glPushMatrix();
		GL11.glScalef(mult, mult, 1.0F);
		this.fontRendererObj.drawString(str, (int) ((x + this.guiLeft) / mult),
				(int) ((y + this.guiTop) / mult), color);
		
		GL11.glPopMatrix();
	}
	
	public void drawStringWithShadow(String str, int x, int y, float mult, int color) {
		GL11.glPushMatrix();
		GL11.glScalef(mult, mult, 1.0F);
		this.fontRendererObj.drawStringWithShadow(str, (int) ((x + this.guiLeft) / mult),
				(int) ((y + this.guiTop) / mult), color);
		
		GL11.glPopMatrix();
	}
	
	public boolean inBounds(int x, int y, int w, int h, int mX, int mY) {
		return (x <= mX) && (mX <= x + w) && (y <= mY) && (mY <= y + h);
	}
	
	public void drawCursor(int x, int y, int z, float size, int color) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, z);
		x += this.guiLeft;
		y += this.guiTop;
		GL11.glTranslatef(x, y, 0.0F);
		GL11.glScalef(size, size, 0.0F);
		GL11.glTranslatef(-x, -y, 0.0F);
		Gui.drawRect(x, y + 1, x + 1, y + 10, color);
		GL11.glPopMatrix();
	}
	
	public void drawString(List<String> str, int x, int y, float mult, int color) {
		drawString(str, 0, str.size(), x, y, mult, color);
	}
	
	public void drawString(List<String> str, int start, int length, int x, int y, float mult,
			int color) {
		GL11.glPushMatrix();
		GL11.glScalef(mult, mult, 1.0F);
		start = Math.max(start, 0);
		int end = Math.min(start + length, str.size());
		for (int i = start; i < end; i++) {
			this.fontRendererObj.drawString((String) str.get(i), (int) ((x + this.guiLeft) / mult),
					(int) ((y + this.guiTop) / mult), color);
			y += this.fontRendererObj.FONT_HEIGHT;
		}
		GL11.glPopMatrix();
	}
	
	public void drawCenteredString(String str, int x, int y, float mult, int width, int height,
			int color) {
		drawString(str, x + (width - (int) (this.fontRendererObj.getStringWidth(str) * mult)) / 2,
				y + (height - (int) ((this.fontRendererObj.FONT_HEIGHT - 2) * mult)) / 2, mult,
				color);
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public List<String> getLinesFromText(String str, float mult, int width) {
		List lst = new ArrayList();
		if (str == null) {
			str = "No info.";
		}
		String[] lines = str.split("\n");
		for (String line : lines) {
			String[] words = line.split(" ");
			String spaceTail = null;
			if (line.endsWith(" ")) {
				spaceTail = "";
				for (int i = line.length() - 1; i >= 0; i--) {
					char c = line.charAt(i);
					if (c != ' ') break;
					spaceTail = spaceTail + c;
				}
				
			}
			
			String currentLine = null;
			for (int i = 0; i < (spaceTail == null ? words.length : words.length + 1); i++) {
				String word;
				if (i == words.length)
					word = spaceTail;
				else
					word = words[i];
				String newLine;
				if (currentLine == null)
					newLine = word;
				else {
					newLine = currentLine + " " + word;
				}
				if (this.fontRendererObj.getStringWidth(newLine) * mult < width) {
					currentLine = newLine;
				}
				else {
					lst.add(currentLine);
					currentLine = word;
				}
			}
			lst.add(currentLine);
		}
		
		return lst;
	}
	
	// ~~~~~~~~~~~ End Code! ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
}
