package com.countrygamer.core.Base.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.countrygamer.core.common.lib.CoreReference;

public class GuiScreenBase extends GuiScreen {
	
	protected final int			grayTextColor	= 4210752;
	
	protected int				xSize			= 176;
	protected int				ySize			= 166;
	protected int				guiLeft;
	protected int				guiTop;
	
	private String				title			= "";
	private ResourceLocation	bkgdTex			= null;
	
	private List<GuiTextField>	textFieldList	= new ArrayList<GuiTextField>();
	
	public GuiScreenBase() {
		this("");
	}
	
	public GuiScreenBase(String title) {
		this(title, new ResourceLocation(CoreReference.MOD_ID, "textures/gui/blank.png"));
	}
	
	public GuiScreenBase(String title, ResourceLocation background) {
		super();
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
	public void drawScreen(int par1, int par2, float par3) {
		this.drawGuiContainerBackgroundLayer(par3, par1, par2);
		this.drawGuiContainerForegroundLayer(par1, par2);
		
		super.drawScreen(par1, par2, par3);
		
		this.drawHoverInfo(par1, par2);
		
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		if (!this.title.equals(""))
			this.drawTitle(
					this.guiLeft + (this.xSize / 2)
							- (this.fontRendererObj.getStringWidth(this.title) / 2), this.guiTop + 5);
		
		this.foregroundText();
		
	}
	
	protected void drawTitle(int titleX, int titleY) {
		this.fontRendererObj.drawString(this.title, titleX, titleY, this.grayTextColor);
	}
	
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0,
				this.xSize, this.ySize);
		
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
	
	private void drawHoverInfo(int mouseX, int mouseY) {
		List<String> hoverInfo = new ArrayList<String>();
		
		this.addHoverInfomation(mouseX, mouseY, hoverInfo);
		
		if (!hoverInfo.isEmpty()) this.renderHoverTip(hoverInfo, mouseX, mouseY);
	}
	
	public void addHoverInfomation(int mouseX, int mouseY, List<String> currentInfomation) {
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
	protected boolean isMouseOver(int x, int y, int w, int h, int mX, int mY) {
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
	
}
