package com.countrygamer.core.Base.client.gui;

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

import com.countrygamer.core.Base.common.inventory.ContainerBase;
import com.countrygamer.core.Base.common.inventory.InventoryItemBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;
import com.countrygamer.core.common.lib.CoreReference;

public abstract class GuiContainerBase extends GuiContainer {
	
	protected String title = "";
	protected int titleX, titleY;
	protected ResourceLocation bkgdTex = null;
	
	protected final int grayTextColor = 4210752;
	
	protected ArrayList<GuiTextField> textFieldList = new ArrayList<GuiTextField>();
	
	public GuiContainerBase(int xSize, int ySize, ContainerBase container) {
		super(container);
		this.xSize = xSize <= 0 ? 176 : xSize;
		this.ySize = ySize <= 0 ? 166 : ySize;
		
	}
	
	protected EntityPlayer getPlayer() {
		return ((ContainerBase) this.inventorySlots).player;
	}
	
	protected TileEntityInventoryBase getTileEntity() {
		return ((ContainerBase) this.inventorySlots).getTileEntity();
	}
	
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
	
	protected void buttonPress(int id) {
	}
	
	protected void setupGui(String title, ResourceLocation backgroundTexture) {
		this.title = title;
		this.bkgdTex = backgroundTexture != null ? backgroundTexture : new ResourceLocation(
				CoreReference.MOD_ID, "textures/gui/blank.png");
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
		if (!this.title.equals(""))
			this.drawTitle(
					(this.xSize / 2) - (this.fontRendererObj.getStringWidth(this.title) / 2), 5);
		
		this.foregroundText();
	}
	
	protected abstract void foregroundText();
	
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
	
	protected abstract void backgroundObjects();
	
	protected void string(String str, int x, int y) {
		this.string(str, x, y, this.grayTextColor);
	}
	
	protected void string(String str, int x, int y, int color) {
		this.fontRendererObj.drawString(str, x, y, color);
	}
	
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
	
	protected abstract void addHoverInfomation(int mouseX, int mouseY, List<String> hoverInfo);
	
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
