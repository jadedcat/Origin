package com.temportalist.origin.wrapper.client.gui

import java.util

import com.temportalist.origin.library.common.utility.Generic
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiContainerWrapper(c: Container) extends GuiContainer(c) with IGuiScreen {

	override protected def addButton(button: GuiButton): Unit = {
		Generic.addToList(this.buttonList, button)
	}

	override protected def renderHoverInformation(mouseX: Int, mouseY: Int,
			hoverInfo: util.List[String]): Unit = {
		this.drawHoveringText(hoverInfo, mouseX, mouseY)
		this.drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj)
	}

	override def drawString(string: String, x: Int, y: Int, color: Int): Unit = {
		this.fontRendererObj.drawString(string, x, y, color)
	}

	override def getStringWidth(string: String): Int = {
		this.fontRendererObj.getStringWidth(string)
	}

	override protected def setSize(x: Int, y: Int): Unit = {
		super.setSize(x, y)
		this.xSize = this.getWidth()
		this.ySize = this.getHeight()
	}

	override def getX(): Int = {
		super.getX()
		this.guiLeft = this.guiX
		this.guiLeft
	}

	override def getY(): Int = {
		super.getY()
		this.guiTop = this.guiY
		this.guiTop
	}

	override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int,
			mouseY: Int): Unit = {
		this.drawGuiBackgroundLayer(mouseX, mouseY, partialTicks)
	}

	override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int): Unit = {
		this.drawGuiForegroundLayer(mouseX, mouseY, 1F)
		this.drawHoverInformation(mouseX, mouseY, 1F)
	}

}
