package com.temportalist.origin.api.client.utility

import cpw.mods.fml.client.registry.{ClientRegistry, RenderingRegistry}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{Gui, GuiScreen, ScaledResolution}
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.{RenderEntity, RenderManager}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.{IItemRenderer, MinecraftForgeClient}
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object Rendering {

	def mc: Minecraft = Minecraft.getMinecraft

	def renderManager: RenderManager = RenderManager.instance

	def thePlayer = this.mc.thePlayer

	//def blockDispatcher: BlockRendererDispatcher = this.mc.getBlockRendererDispatcher

	//def blockShapes: BlockModelShapes = this.blockDispatcher.getBlockModelShapes

	//def renderItem: RenderItem = this.mc.rende

	//def itemMesher: ItemModelMesher = this.renderItem.getItemModelMesher

	def display(gui: GuiScreen): Unit = this.mc.displayGuiScreen(gui)

	def bindResource(rl: ResourceLocation): Unit = {
		Rendering.mc.getTextureManager.bindTexture(rl)
	}

	def drawTextureAtSize(pos: (Int, Int), texDim: (Int, Int), uv: (Int, Int), imgDim: (Int, Int)): Unit = {
		Gui.func_146110_a(pos._1, pos._2, uv._1, uv._2, imgDim._1, imgDim._2, texDim._1, texDim._2)
	}

	def drawTextureWithSizes(pos: (Int, Int), uv: (Float, Float), actualSize: (Int, Int),
			renderedSize: (Int, Int), imgSize: (Float, Float)): Unit = {
		Gui.func_152125_a(pos._1, pos._2, uv._1, uv._2, actualSize._1, actualSize._2,
			renderedSize._1, renderedSize._2, imgSize._1, imgSize._2)
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

	/*
	def getModel(stack: ItemStack, isItem: Boolean): IBakedModel = {
		if (!isItem && WorldHelper.isBlock(stack.getItem))
			Rendering.blockShapes.getModelForState(States.getState(stack))
		else
			Rendering.itemMesher.getItemModel(stack)
	}
	*/

	def getScaledResoultion: ScaledResolution =
		new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight)

	def registerRender(entity: Class[_ <: Entity], renderer: RenderEntity): Unit =
		RenderingRegistry.registerEntityRenderingHandler(entity, renderer)

	def registerRender(tile: Class[_ <: TileEntity], renderer: TileEntitySpecialRenderer): Unit =
		ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer)

	def registerRender(item: Item, renderer: IItemRenderer): Unit =
		MinecraftForgeClient.registerItemRenderer(item, renderer)

	def registerRender(block: Block, renderer: IItemRenderer): Unit =
		this.registerRender(Item.getItemFromBlock(block), renderer)

	object Gl {

		def color(r: Float, g: Float, b: Float): Unit = GL11.glColor3f(r, g, b)

		def color(r: Double, g: Double, b: Double): Unit = GL11.glColor3d(r, g, b)

		def color(r: Float, g: Float, b: Float, a: Float): Unit = GL11.glColor4f(r, g, b, a)

		def color(r: Double, g: Double, b: Double, a: Double): Unit = GL11.glColor4d(r, g, b, a)

		def colorFull(): Unit = this.color(1, 1, 1, 1)

		def enable(i: Int, isOn: Boolean): Unit = if (isOn) GL11.glEnable(i) else GL11.glDisable(i)

		def blend(isOn: Boolean): Unit = this.enable(GL11.GL_BLEND, isOn)

		def blendFunc(a: Int, b: Int, c: Int, d: Int): Unit = OpenGlHelper.glBlendFunc(a, b, c, d)

		def blendFunc(typeA: Int, typeB: Int): Unit = GL11.glBlendFunc(typeA, typeB)

		def blendSrcAlpha(): Unit = this.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

	}

}
