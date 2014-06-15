package com.countrygamer.countrygamercore.Base.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.countrygamer.countrygamercore.Base.common.inventory.ContainerBase;
import com.countrygamer.countrygamercore.Base.common.inventory.InventoryItemBase;
import com.countrygamer.countrygamercore.Base.common.tile.TileEntityInventoryBase;
import com.countrygamer.countrygamercore.common.Core;

/**
 * A base class for all GuiContainers
 * TODO, move {@link GuiContainer} to here so that this can extend {@link GuiScreenBase}
 * 
 * @author Country_Gamer
 * 
 */
public abstract class GuiContainerBase extends GuiContainer {
	
	protected String title = "";
	protected int titleX, titleY;
	protected ResourceLocation bkgdTex = null;
	
	protected final int grayTextColor = 4210752;
	
	protected ArrayList<GuiTextField> textFieldList = new ArrayList<GuiTextField>();
	
	public GuiContainerBase(ContainerBase container) {
		this(176, 166, container);
	}
	
	public GuiContainerBase(int xSize, int ySize, ContainerBase container) {
		super(container);
		this.xSize = xSize <= 0 ? 176 : xSize;
		this.ySize = ySize <= 0 ? 166 : ySize;
		
	}
	
	/**
	 * Get the player who opened this gui
	 * 
	 * @return
	 */
	protected EntityPlayer getPlayer() {
		return ((ContainerBase) this.inventorySlots).player;
	}
	
	/**
	 * Get the tile entity that this gui was opened from
	 * 
	 * @return
	 */
	protected TileEntityInventoryBase getTileEntity() {
		return ((ContainerBase) this.inventorySlots).getTileEntity();
	}
	
	/**
	 * Get the item that this gui was opened from
	 * 
	 * @return
	 */
	protected InventoryItemBase getItemInventory() {
		return ((ContainerBase) this.inventorySlots).getItemInventory();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton gB) {
		int id = gB.id;
		this.buttonPress(id);
		
	}
	
	/**
	 * Triggered when a button is pressed in this gui
	 * 
	 */
	protected abstract void buttonPress(int id);
	
	/**
	 * Save info about this gui.
	 * 
	 * @param title
	 *            - the title of this gui
	 * @param backgroundTexture
	 *            - the background picture
	 */
	protected void setupGui(String title, ResourceLocation backgroundTexture) {
		this.title = title;
		this.bkgdTex = backgroundTexture != null ? backgroundTexture : new ResourceLocation(
				Core.pluginID, "textures/gui/blank.png");
	}
	
	/**
	 * Add a text field to this gui
	 * 
	 * @param textField
	 * @param maxStrLength
	 */
	protected void setupTextField(GuiTextField textField, int maxStrLength) {
		textField.setTextColor(-1);
		textField.setDisabledTextColour(-1);
		textField.setEnableBackgroundDrawing(true);
		textField.setMaxStringLength(maxStrLength);
		textField.setText("");
		this.textFieldList.add(textField);
	}
	
	@Override
	protected void keyTyped(char letter, int keycode) {
		boolean wasField = false;
		for (GuiTextField field : this.textFieldList) {
			if (field.textboxKeyTyped(letter, keycode)) {
				this.sendKeyPacket(field);
				wasField = true;
			}
		}
		if (!wasField) {
			super.keyTyped(letter, keycode);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		for (GuiTextField field : this.textFieldList) {
			field.mouseClicked(x, y, button);
		}
	}
	
	protected void sendKeyPacket(GuiTextField txtField) {
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
		super.updateScreen();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.drawHoverInfo(par1, par2);
	}
	
	@Override
	protected final void drawGuiContainerForegroundLayer(int par1, int par2) {
		if (!this.title.equals("")) this.renderTitle();
		
		this.foregroundText();
	}
	
	/**
	 * Used to draw foreground objects (like text)
	 */
	protected abstract void foregroundText();
	
	protected void renderTitle() {
		this.drawTitle((this.xSize / 2) - (this.fontRendererObj.getStringWidth(this.title) / 2), 5);
	}
	
	protected final void drawTitle(int titleX, int titleY) {
		this.titleX = titleX;
		this.titleY = titleY;
		this.fontRendererObj.drawString(this.title, this.titleX, this.titleY, this.grayTextColor);
	}
	
	@Override
	protected final void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.backgroundObjects();
	}
	
	/**
	 * Used to draw background objects (like buttons, text fields, or graphics)
	 */
	protected abstract void backgroundObjects();
	
	/**
	 * Draw a string to the gui
	 * 
	 * @param str
	 * @param x
	 * @param y
	 */
	protected void string(String str, int x, int y) {
		this.string(str, x, y, this.grayTextColor);
	}
	
	/**
	 * Draw a string to the gui with color
	 * 
	 * @param str
	 * @param x
	 * @param y
	 * @param color
	 */
	protected void string(String str, int x, int y, int color) {
		this.fontRendererObj.drawString(str, x, y, color);
	}
	
	/**
	 * Draw infomation from this gui on player hover
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void drawHoverInfo(int mouseX, int mouseY) {
		List<String> hoverInfo = new ArrayList<String>();
		
		this.addHoverInfomation(mouseX, mouseY, hoverInfo);
		
		if (!hoverInfo.isEmpty()) this.renderHoverTip(hoverInfo, mouseX, mouseY);
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
	protected final boolean isMouseOver(int x, int y, int w, int h, int mX, int mY) {
		return mX >= x && mX < x + w && mY >= y && mY < y + h;
	}
	
	/**
	 * Add infomation to the hover list of this gui
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param hoverInfo
	 */
	protected abstract void addHoverInfomation(int mouseX, int mouseY, List<String> hoverInfo);
	
	/**
	 * Renderer for hover information
	 * 
	 * @param hoverInfo
	 * @param mouseX
	 * @param mouseY
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	private void renderHoverTip(List hoverInfo, int mouseX, int mouseY) {
		for (int k = 0; k < hoverInfo.size(); ++k) {
			hoverInfo.set(k, EnumChatFormatting.GRAY + (String) hoverInfo.get(k));
		}
		
		this.func_146283_a(hoverInfo, mouseX, mouseY);
		drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj);
	}
	
}
