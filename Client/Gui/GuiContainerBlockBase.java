package com.countrygamer.countrygamer_core.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.countrygamer_core.block.tiles.TileEntityInventoryBase;
import com.countrygamer.countrygamer_core.inventory.ContainerBlockBase;
import com.countrygamer.countrygamer_core.lib.CoreReference;

public class GuiContainerBlockBase extends GuiContainer {
	
	public TileEntityInventoryBase		tileEnt;
	
	protected int						leftOfGui		= (this.width - this.xSize) / 2;
	protected int						topOfGui		= (this.height - this.ySize) / 2;
	protected final int					grayTextColor	= 4210752;
	protected String					title			= "";
	protected int						titleX, titleY;
	private ResourceLocation			bkgdTex			= null;
	
	protected final EntityPlayer				thePlayer;
	protected ArrayList<GuiTextField>	textFieldList	= new ArrayList<GuiTextField>();
	
	public GuiContainerBlockBase(EntityPlayer player, TileEntityInventoryBase tileEnt) {
		this(player, new ContainerBlockBase(player.inventory, tileEnt));
	}
	
	public GuiContainerBlockBase(EntityPlayer player, ContainerBlockBase container) {
		super(container);
		this.thePlayer = player;
		this.tileEnt = container.tileEnt;
		this.setupGui("", new ResourceLocation(CoreReference.MOD_ID, "textures/gui/blank.png"));
	}
	
	protected void setupGui(String title, ResourceLocation backgroundTexture) {
		this.title = title;
		this.bkgdTex = backgroundTexture;
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
		this.leftOfGui = (this.width - this.xSize) / 2;
		this.topOfGui = (this.height - this.ySize) / 2;
		drawTexturedModalRect(this.leftOfGui, this.topOfGui, 0, 0, this.xSize, this.ySize);
		this.backgroundObjects();
	}
	
	public void foregroundText() {
	}
	
	public void backgroundObjects() {
		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
	}
	
	protected void string(String str, int x, int y) {
		this.string(str, x, y, this.grayTextColor);
	}
	
	protected void string(String str, int x, int y, int color) {
		this.fontRendererObj.drawString(str, x, y, color);
	}
	
}
