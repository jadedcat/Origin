package com.temportalist.origin.library.client.utility

import com.temportalist.origin.library.common.utility.{States, WorldHelper}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer._
import net.minecraft.client.renderer.entity.{RenderItem, RenderManager}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object Rendering {

	def mc: Minecraft = Minecraft.getMinecraft

	def renderManager: RenderManager = this.mc.getRenderManager

	def blockDispatcher: BlockRendererDispatcher = this.mc.getBlockRendererDispatcher

	def blockShapes: BlockModelShapes = this.blockDispatcher.getBlockModelShapes

	def renderItem: RenderItem = this.mc.getRenderItem

	def itemMesher: ItemModelMesher = this.renderItem.getItemModelMesher

	def bindResource(rl: ResourceLocation): Unit = {
		Rendering.mc.getTextureManager.bindTexture(rl)
	}

	def drawTextureRect(x: Int, y: Int, u: Int, v: Int, width: Int, height: Int): Unit = {
		// todo this is super bugged
		val f: Float = 0.00390625F
		val f1: Float = f
		val z: Int = -90
		TessRenderer.startQuads()
		TessRenderer.addVertex(
			(x + 0).asInstanceOf[Double],
			(y + height).asInstanceOf[Double],
			z,
			((u + 0).asInstanceOf[Float] * f).asInstanceOf[Double],
			((v + height).asInstanceOf[Float] * f1).asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			(x + width).asInstanceOf[Double],
			(y + height).asInstanceOf[Double],
			z,
			((u + width).asInstanceOf[Float] * f).asInstanceOf[Double],
			((v + height).asInstanceOf[Float] * f1).asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			(x + width).asInstanceOf[Double],
			(y + 0).asInstanceOf[Double],
			z,
			((u + width).asInstanceOf[Float] * f).asInstanceOf[Double],
			((v + 0).asInstanceOf[Float] * f1).asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			(x + 0).asInstanceOf[Double],
			(y + 0).asInstanceOf[Double],
			z,
			((u + 0).asInstanceOf[Float] * f).asInstanceOf[Double],
			((v + 0).asInstanceOf[Float] * f1).asInstanceOf[Double]
		)


		TessRenderer.draw()
	}

	def drawTextureWithOffsets(x: Int, y: Int, u: Int, v: Int, w: Int, h: Int,
			leftOffset: Int, rightOffset: Int, topOffset: Int, bottomOffset: Int): Unit = {
		Rendering.drawTextureRect(
			x + leftOffset,
			y + topOffset,
			u + leftOffset,
			v + topOffset,
			w - rightOffset - leftOffset,
			h - bottomOffset - topOffset
		)
	}

	def getSprite(iconName: String): TextureAtlasSprite =
		Rendering.mc.getTextureMapBlocks.getAtlasSprite(iconName)

	def drawSprite(x: Double, y: Double, z: Double, location: ResourceLocation, w: Double,
			h: Double): Unit = {
		this.drawSprite(x, y, z, this.getSprite(location.toString), w, h)
	}

	def drawSprite(x: Double, y: Double, z: Double, sprite: TextureAtlasSprite, w: Double,
			h: Double): Unit = {
		TessRenderer.startQuads()
		TessRenderer.addVertex(
			x + 0,
			y + h,
			z,
			sprite.getMinU.asInstanceOf[Double],
			sprite.getMaxV.asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			x + w,
			y + h,
			z,
			sprite.getMaxU.asInstanceOf[Double],
			sprite.getMaxV.asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			x + w,
			y + 0,
			z,
			sprite.getMaxU.asInstanceOf[Double],
			sprite.getMinV.asInstanceOf[Double]
		)
		TessRenderer.addVertex(
			x + 0,
			y + 0,
			z,
			sprite.getMinU.asInstanceOf[Double],
			sprite.getMinV.asInstanceOf[Double]
		)
		TessRenderer.draw()
	}

	def modelCoordsToVerticies(x: Float, y: Float, z: Float, color: Int,
			texture: TextureAtlasSprite, u: Float, v: Float): Array[Int] = {
		Array[Int](
			java.lang.Float.floatToRawIntBits(x),
			java.lang.Float.floatToRawIntBits(y),
			java.lang.Float.floatToRawIntBits(z),
			color,
			java.lang.Float.floatToRawIntBits(texture.getInterpolatedU(u)),
			java.lang.Float.floatToRawIntBits(texture.getInterpolatedV(v)),
			0
		)
	}

	def getModel(stack: ItemStack, isItem: Boolean): IBakedModel = {
		if (!isItem && WorldHelper.isBlock(stack.getItem))
			Rendering.blockShapes.getModelForState(States.getState(stack))
		else
			Rendering.itemMesher.getItemModel(stack)
	}

	def getScaledResoultion(): ScaledResolution =
		new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight)

}
