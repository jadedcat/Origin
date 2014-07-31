package com.countrygamer.cgo.client.gui

import java.util

import com.countrygamer.cgo.client.gui.widget.IWidgetOwner
import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.common.lib.util.UtilRender
import cpw.mods.fml.relauncher.{Side, SideOnly}
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{GuiScreen, GuiTextField}
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.C17PacketCustomPayload
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiScreenWrapper(val xSize: Int, val ySize: Int) extends GuiScreen with IWidgetOwner {

	protected val defaultTextColor: Int = 4210752
	protected var guiLeft: Int = 0
	protected var guiTop: Int = 0

	protected var title: String = null
	private var background: ResourceLocation = null

	private val textFieldList: util.List[GuiTextField] = new util.ArrayList[GuiTextField]()

	// Default Constructor
	{
		this.setupGui(null, null)
		this.mc = Minecraft.getMinecraft
	}

	// End Constructor

	// Other Constructors
	def this() {
		this(176, 166)
	}

	// End Constructors

	// ~~~~~~~~~~~~~~~~~~~~~ Override Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def initGui(): Unit = {
		super.initGui()

		this.getGuiLeft()
		this.getGuiTop()

		Keyboard.enableRepeatEvents(true)

	}

	override def keyTyped(letter: Char, key: Int): Unit = {
		var containsField: Boolean = false
		var i: Int = 0
		for (i <- 0 until this.textFieldList.size()) {
			val textField: GuiTextField = this.textFieldList.get(i)
			if (textField.textboxKeyTyped(letter, key)) {
				this.sendKeyPacket(textField)
				containsField = true
			}
		}
		if (!containsField) {
			super.keyTyped(letter, key)
		}
	}

	override def mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Unit = {
		super.mouseClicked(mouseX, mouseY, mouseButton)

		var i: Int = 0
		for (i <- 0 until this.textFieldList.size()) {
			val textField: GuiTextField = this.textFieldList.get(i)
			textField.mouseClicked(mouseX, mouseY, mouseButton)
		}

	}

	override def doesGuiPauseGame(): Boolean = {
		false
	}

	override def onGuiClosed(): Unit = {
		super.onGuiClosed()
		Keyboard.enableRepeatEvents(false)
	}

	override def drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float): Unit = {
		this.drawGuiBackgroundLayer(mouseX, mouseY, renderPartialTicks)
		this.drawGuiForegroundLayer(mouseX, mouseY, renderPartialTicks)

		this.guiScreenDrawScreen(mouseX, mouseY, renderPartialTicks)

		this.drawHoverInformation(mouseX, mouseY, renderPartialTicks)

	}

	// ~~~~~~~~~~~~~~~~~~~~~ Setup methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	def setupGui(title: String, background: ResourceLocation): Unit = {
		this.title = title
		this.background = background

		if (this.title == null)
			this.title = ""

		if (this.background == null) {
			this.background = new ResourceLocation(Origin.pluginID, "textures/gui/blank.png")
		}

	}

	def setupTextField(textField: GuiTextField, maxStringLength: Int): Unit = {
		textField.setTextColor(-1)
		textField.setDisabledTextColour(-1)
		textField.setEnableBackgroundDrawing(true)
		textField.setMaxStringLength(maxStringLength)
		textField.setText("")
		this.textFieldList.add(textField)
	}

	// ~~~~~~~~~~~~~~~~~~~~~ Utility Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected def guiScreenDrawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float): Unit = {
		super.drawScreen(mouseX, mouseY, renderPartialTicks)
	}

	private def sendKeyPacket(textField: GuiTextField): Unit = {
		val packetBuffer: PacketBuffer = new PacketBuffer(Unpooled.buffer())
		try {
			packetBuffer.writeStringToBuffer(textField.getText)
			this.mc.getNetHandler
					.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", packetBuffer))
		}
		catch {
			case e: Exception =>
				LogManager.getLogger.error("Couldn\'t send text field info", e)
		}
		finally {
			packetBuffer.release()
		}

	}

	protected def drawGuiBackgroundLayer(mouseX: Int, mouseY: Int,
			renderPartialTicks: Float): Unit = {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)

		this.drawGuiBackground()

		var i: Int = 0
		for (i <- 0 until this.textFieldList.size()) {
			val textField: GuiTextField = this.textFieldList.get(i)
			textField.drawTextBox()
		}

	}

	protected def drawGuiBackground(): Unit = {
		UtilRender.bindResource(this.getBackgound())
		this.drawTexturedModalRect(this.getGuiLeft(), this.getGuiTop(), 0, 0, this.xSize,
			this.ySize)
	}

	def getBackgound(): ResourceLocation = {
		this.background
	}

	protected def drawGuiForegroundLayer(mouseX: Int, mouseY: Int,
			renderPartialTicks: Float): Unit = {
		if (this.title != null) {
			this.drawTitle(
				this.getGuiLeft + (this.xSize / 2) - (this.getStringWidth(this.title) / 2),
				this.getGuiTop + 5
			)
		}

	}

	def drawTitle(x: Int, y: Int): Unit = {
		this.drawString(this.title, x, y)
	}

	def drawString(string: String, x: Int, y: Int): Unit = {
		this.drawString(string, x, y, this.defaultTextColor)
	}

	def drawString(string: String, x: Int, y: Int, color: Int): Unit = {
		this.fontRendererObj.drawString(string, x, y, color)
	}

	override def getXSize(): Int = {
		this.xSize
	}

	override def getYSize(): Int = {
		this.ySize
	}

	override def getGuiLeft(): Int = {
		this.guiLeft = (this.width - this.xSize) / 2
		this.guiLeft
	}

	override def getGuiTop(): Int = {
		this.guiTop = (this.height - this.ySize) / 2
		this.guiTop
	}

	def getStringWidth(string: String): Int = {
		this.fontRendererObj.getStringWidth(string)
	}

	private def drawHoverInformation(mouseX: Int, mouseY: Int,
			renderPartialTicks: Float): Unit = {
		val hoverInfo: util.List[String] = new util.ArrayList[String]()

		this.addInformationOnHover(mouseX, mouseY, renderPartialTicks, hoverInfo)

		if (!hoverInfo.isEmpty) {
			this.renderHoverInformation(mouseX, mouseY, hoverInfo)
		}
	}

	protected def addInformationOnHover(mouseX: Int, mouseY: Int,
			renderPartialTicks: Float, hoverInfo: util.List[String]): Unit = {
	}

	def isMouseInArea(x: Int, y: Int, w: Int, h: Int, mouseX: Int,
			mouseY: Int): Boolean = {
		(x <= mouseX) && (mouseX <= x + w) && (y <= mouseY) && (mouseY <= y + h)
	}

	private def renderHoverInformation(mouseX: Int, mouseY: Int,
			hoverInfo: util.List[String]): Unit = {
		this.func_146283_a(hoverInfo, mouseX, mouseY)
		this.drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj)
	}

	protected def drawLine(x1: Int, y1: Int, x2: Int, y2: Int, thickness: Int, color: Int): Unit = {
		GL11.glDisable(3553)

		this.applyColor(color)

		GL11.glEnable(2848)
		GL11.glLineWidth(1.0F + thickness * this.width / 500.0F)

		GL11.glBegin(1)

		GL11.glVertex3f(x1, y1, 0.0F)
		GL11.glVertex3f(x2, y2, 0.0F)

		GL11.glEnd()

		GL11.glEnable(3553)
	}

	protected def applyColor(color: Int): Unit = {
		val a: Float = (color >> 24 & 0xFF) / 255.0F
		val r: Float = (color >> 16 & 0xFF) / 255.0F
		val g: Float = (color >> 8 & 0xFF) / 255.0F
		val b: Float = (color & 0xFF) / 255.0F

		GL11.glColor4f(r, g, b, a)
	}

}
