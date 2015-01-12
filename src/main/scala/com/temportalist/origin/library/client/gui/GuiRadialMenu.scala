package com.temportalist.origin.library.client.gui

import java.util

import com.temportalist.origin.library.client.utility.TessRenderer
import com.temportalist.origin.library.common.lib.IRadialSelection
import com.temportalist.origin.wrapper.client.gui.IGuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.{GlStateManager, RenderHelper}
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
abstract class GuiRadialMenu(
		private val innerRadius: Int, private val outerRadius: Int,
		private val selections: util.ArrayList[_ <: IRadialSelection]
		) extends IGuiScreen {

	this.setSize(outerRadius * 2, outerRadius * 2)

	val animationTimer: Int = 20

	var selectedLocalIndex: Int = -1

	def shouldSelect(): Boolean

	def selectCurrent(): Unit = {
		if (this.selectedLocalIndex >= 0) {
			this.sendPacket(this.selectedLocalIndex)
		}
		this.dismiss()
	}

	def sendPacket(index: Int): Unit

	def renderMenu(): Unit = {
		val zLevel: Double = 0.05D
		val resolution: ScaledResolution = new ScaledResolution(
			this.mc, this.mc.displayWidth, this.mc.displayHeight
		)

		val anglePerSection: Double = 360D / this.selections.size().toDouble
		this.renderRadial(resolution, zLevel, this.selections.size(), anglePerSection)
		this.renderIconsAndText(resolution, zLevel, this.selections.size(), anglePerSection)

	}

	def renderRadial(resolution: ScaledResolution, zLevel: Double,
			quantity: Int, anglePer: Double): Unit = {
		GlStateManager.pushMatrix()

		GlStateManager.disableTexture2D()

		GlStateManager.enableBlend()
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

		GL11.glMatrixMode(GL11.GL_MODELVIEW)
		GlStateManager.pushMatrix()
		GlStateManager.loadIdentity()

		GL11.glMatrixMode(GL11.GL_PROJECTION)
		GlStateManager.pushMatrix()
		GlStateManager.loadIdentity()

		val mouseAngle = this.correctAngle(this.getMouseAngle() - 270)

		for (i <- 0 until quantity) {
			var currAngle: Double = anglePer * i
			var nextAngle: Double = currAngle + anglePer
			currAngle = this.correctAngle(currAngle)
			nextAngle = this.correctAngle(nextAngle)

			val isMouseIn: Boolean = mouseAngle > currAngle && mouseAngle < nextAngle

			currAngle = Math.toRadians(currAngle)
			nextAngle = Math.toRadians(nextAngle)

			val innerR: Double =
				((this.innerRadius - animationTimer - (if (isMouseIn) 1 else 2)) / 100F) *
						(257F / resolution.getScaledHeight.toFloat)
			val outerR: Double =
				((this.outerRadius - animationTimer + (if (isMouseIn) 1 else 2)) / 100F) *
						(257F / resolution.getScaledHeight.toFloat)

			if (isMouseIn) {
				GlStateManager.color(28F / 255F, 232F / 255F, 31F / 255F, 153F / 255F)
				this.selectedLocalIndex = i
			}
			else {
				GlStateManager.color(0F / 255F, 0F / 255F, 0F / 255F, 153F / 255F)
			}

			TessRenderer.startQuads()

			TessRenderer.addVertex(
				Math.cos(currAngle) * resolution.getScaledHeight_double() /
						resolution.getScaledWidth_double() * innerR,
				Math.sin(currAngle) * innerR, 0
			)
			TessRenderer.addVertex(
				Math.cos(currAngle) * resolution.getScaledHeight_double() /
						resolution.getScaledWidth_double() * outerR,
				Math.sin(currAngle) * outerR, 0
			)
			TessRenderer.addVertex(
				Math.cos(nextAngle) * resolution.getScaledHeight_double() /
						resolution.getScaledWidth_double() * outerR,
				Math.sin(nextAngle) * outerR, 0
			)
			TessRenderer.addVertex(
				Math.cos(nextAngle) * resolution.getScaledHeight_double() /
						resolution.getScaledWidth_double() * innerR,
				Math.sin(nextAngle) * innerR, 0
			)

			TessRenderer.draw()

		}

		GlStateManager.popMatrix()
		GL11.glMatrixMode(GL11.GL_MODELVIEW)
		GlStateManager.popMatrix()

		GlStateManager.disableBlend()

		GlStateManager.enableTexture2D()

		GlStateManager.popMatrix()
	}

	def renderIconsAndText(resolution: ScaledResolution, zLevel: Double,
			quantity: Int, anglePer: Double): Unit = {
		GlStateManager.pushMatrix()

		GlStateManager.translate(resolution.getScaledWidth_double() / 2,
			resolution.getScaledHeight_double() / 2, 0)
		RenderHelper.enableGUIStandardItemLighting()

		//val tessellator: Tessellator = Tessellator.getInstance()

		// todo bind what used to be TextureMap.locationItemsTexture
		//this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		//Rendering.bindResource(TextureMap.)

		var selection: IRadialSelection = null
		for (i <- 0 until quantity) {
			selection = this.selections.get(i)
			if (selection != null) {
				val angle: Double = (anglePer * i * -1) - anglePer / 2
				val drawOffset: Double = 2.0D
				var drawX: Double = this.innerRadius - this.animationTimer + drawOffset
				var drawY: Double = this.innerRadius - this.animationTimer + drawOffset
				val length: Double = Math.sqrt(drawX * drawX + drawY + drawY)

				drawX = length * Math.cos(StrictMath.toRadians(angle))
				drawY = length * Math.sin(StrictMath.toRadians(angle))
				val dif: Double = this.outerRadius.toDouble / this.innerRadius.toDouble
				val iconX: Double = drawX * dif
				val iconY: Double = drawY * dif * 0.9D

				selection.draw(this.mc, iconX, iconY, zLevel, 32, 32)
			}
		}
		RenderHelper.disableStandardItemLighting()

		GlStateManager.popMatrix()
	}

	def getMouseAngle(): Double = {
		getRelativeAngle(
			this.mc.displayWidth / 2, this.mc.displayHeight / 2, Mouse.getX, Mouse.getY
		)
	}

	def getRelativeAngle(originX: Double, originY: Double, x: Double, y: Double): Double = {
		var angle: Double = Math.toDegrees(Math.atan2(y - originY, x - originX))

		// Remove 90 from the angle to make 0 and 180 at the top and bottom of the screen
		angle = angle - 90

		if (angle < 0) {
			angle = angle + 360
		}
		else if (angle > 360) {
			angle = angle - 360
		}

		angle
	}

	def correctAngle(angle: Double): Double = {
		var angle2: Double = angle
		if (angle < 0) {
			angle2 = angle + 360
		}
		else if (angle > 360) {
			angle2 = angle - 360
		}

		angle2
	}

	final def dismiss(): Unit = {
		this.mc.displayGuiScreen(null)
	}

	override def drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float): Unit = {}

}
