package com.countrygamer.core.Base.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;
import com.countrygamer.core.common.lib.CoreReference;

public class GuiContainerBlockBase extends GuiContainer {
	
	public TileEntityInventoryBase tileEnt;
	
	protected int leftOfGui = (this.width - this.xSize) / 2;
	protected int topOfGui = (this.height - this.ySize) / 2;
	protected final int grayTextColor = 4210752;
	protected String title = "";
	protected int titleX, titleY;
	protected ResourceLocation bkgdTex = null;
	
	protected final EntityPlayer thePlayer;
	protected ArrayList<GuiTextField> textFieldList = new ArrayList<GuiTextField>();
	
	public GuiContainerBlockBase(EntityPlayer player, TileEntityInventoryBase tileEnt) {
		this(player, new ContainerBlockBase(player.inventory, tileEnt));
	}
	
	public GuiContainerBlockBase(EntityPlayer player, ContainerBlockBase container) {
		super(container);
		this.thePlayer = player;
		this.tileEnt = container.tileEnt;
		this.setupGui("", null);
	}
	
	protected void setupGui(String title, ResourceLocation backgroundTexture) {
		this.title = title;
		this.bkgdTex = backgroundTexture != null ? backgroundTexture : new ResourceLocation(
				CoreReference.MOD_ID, "textures/gui/blank.png");
	}
	
	@Override
	public void initGui() {
		super.initGui();
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
		if (!this.title.equals(""))
			this.drawTitle(
					(this.xSize / 2) - (this.fontRendererObj.getStringWidth(this.title) / 2), 5);
		
		this.foregroundText();
	}
	
	protected void drawTitle(int titleX, int titleY) {
		this.titleX = titleX;
		this.titleY = titleY;
		this.fontRendererObj.drawString(this.title, this.titleX, this.titleY, this.grayTextColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		
		this.backgroundObjects();
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
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void renderHoverTip(List hoverInfo, int mouseX, int mouseY) {
		for (int k = 0; k < hoverInfo.size(); ++k) {
			hoverInfo.set(k, EnumChatFormatting.GRAY + (String) hoverInfo.get(k));
		}
		
		this.func_146283_a(hoverInfo, mouseX, mouseY);
		drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj);
	}
	
}
