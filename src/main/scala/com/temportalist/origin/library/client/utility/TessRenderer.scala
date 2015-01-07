package com.temportalist.origin.library.client.utility

import net.minecraft.client.renderer.{WorldRenderer, Tessellator}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object TessRenderer {

	def getTess(): Tessellator = Tessellator.getInstance()

	def getRenderer(): WorldRenderer = this.getTess().getWorldRenderer

	def startQuads(): Unit = this.getRenderer().startDrawingQuads()

	def draw(): Unit = this.getTess().draw()

	def addVertex(x: Double, y: Double, z: Double, u: Double, v: Double): Unit =
		this.getRenderer().addVertexWithUV(x, y, z, y, v)

	def addVertex(x: Double, y: Double, z: Double): Unit =
		this.getRenderer().addVertex(x, y, z)

}
