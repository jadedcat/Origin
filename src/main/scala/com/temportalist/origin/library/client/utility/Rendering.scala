package com.temportalist.origin.library.client.utility

import net.minecraft.client.renderer.{WorldRenderer, Tessellator}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.ResourceLocation

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object Rendering {

	def bindResource(rl: ResourceLocation): Unit = {
		Minecraft.getMinecraft.getTextureManager.bindTexture(rl)
	}

	def drawTextureWithOffsets(gui: Gui, x: Int, y: Int, u: Int, v: Int, w: Int, h: Int,
			leftOffset: Int, rightOffset: Int, topOffset: Int, bottomOffset: Int): Unit = {
		gui.drawTexturedModalRect(
			x + leftOffset,
			y + topOffset,
			u + leftOffset,
			v + topOffset,
			w - rightOffset - leftOffset,
			h - bottomOffset - topOffset
		)
	}

	def drawSprite(x: Double, y: Double, z: Double, sprite: TextureAtlasSprite, w: Double, h: Double): Unit = {
		val tessellator: Tessellator = Tessellator.getInstance
		val worldrenderer: WorldRenderer = tessellator.getWorldRenderer
		worldrenderer.startDrawingQuads
		worldrenderer.addVertexWithUV(
			x + 0,
			y + h,
			z,
			sprite.getMinU.asInstanceOf[Double],
			sprite.getMaxV.asInstanceOf[Double]
		)
		worldrenderer.addVertexWithUV(
			x + w,
			y + h,
			z,
			sprite.getMaxU.asInstanceOf[Double],
			sprite.getMaxV.asInstanceOf[Double]
		)
		worldrenderer.addVertexWithUV(
			x + w,
			y + 0,
			z,
			sprite.getMaxU.asInstanceOf[Double],
			sprite.getMinV.asInstanceOf[Double]
		)
		worldrenderer.addVertexWithUV(
			x + 0,
			y + 0,
			z,
			sprite.getMinU.asInstanceOf[Double],
			sprite.getMinV.asInstanceOf[Double]
		)
		tessellator.draw
	}

	def modelCoordsToVerticies(x: Float, y: Float, z: Float, color: Int, texture: TextureAtlasSprite, u: Float, v: Float): Array[Int] = {
		Array[Int] (
			java.lang.Float.floatToRawIntBits(x),
			java.lang.Float.floatToRawIntBits(y),
			java.lang.Float.floatToRawIntBits(z),
			color,
			java.lang.Float.floatToRawIntBits(texture.getInterpolatedU(u)),
			java.lang.Float.floatToRawIntBits(texture.getInterpolatedV(v)),
			0
		)
	}

}
