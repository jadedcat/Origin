package com.countrygamer.core.Base.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.countrygamer.core.Base.inventory.ContainerItemBase;
import com.countrygamer.core.Base.inventory.InventoryItemBase;
import com.countrygamer.core.lib.CoreReference;

public class GuiContainerItemBase extends GuiContainer {
	
	/** The inventory to render on screen */
	protected final InventoryItemBase	inventory;
	protected final int					grayTextColor	= 4210752;
	protected String					title			= "";
	protected ResourceLocation			bkgdTex			= null;
	
	protected ArrayList<GuiTextField>		textFieldList	= new ArrayList<GuiTextField>();
	
	public GuiContainerItemBase(EntityPlayer player, InventoryPlayer invPlayer,
			InventoryItemBase inventory) {
		this(new ContainerItemBase(player, invPlayer, inventory));
	}
	
	public GuiContainerItemBase(EntityPlayer player, InventoryPlayer invPlayer,
			InventoryItemBase inventory, int xSize, int ySize) {
		this(player, invPlayer, inventory);
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public GuiContainerItemBase(ContainerItemBase container) {
		super(container);
		this.inventory = (InventoryItemBase) container.inventory;
		this.title = "";
		this.bkgdTex = new ResourceLocation(CoreReference.MOD_ID, "textures/gui/blank.png");
	}
	
	public GuiContainerItemBase(ContainerItemBase container, int xSize, int ySize) {
		this(container);
		this.xSize = xSize;
		this.ySize = ySize;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	protected void actionPerformed(GuiButton gB) {
		int id = gB.id;
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
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.renderTitle();
		this.foregroundText();
	}
	
	protected void renderTitle() {
		this.fontRendererObj.drawString(this.title,
				(this.xSize / 2) - (this.fontRendererObj.getStringWidth(this.title) / 2), 5,
				this.grayTextColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		
		this.backgroundObjects();
	}
	
	public void foregroundText() {
	}
	
	public void backgroundObjects() {
	}
	
	protected void string(String str, int x, int y) {
		this.string(str, x, y, this.grayTextColor);
	}
	
	protected void string(String str, int x, int y, int color) {
		this.fontRendererObj.drawString(str, x, y, color);
	}
	
}
