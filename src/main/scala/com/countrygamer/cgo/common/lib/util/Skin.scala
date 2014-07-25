package com.countrygamer.cgo.common.lib.util

import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.common.lib.LogHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
class Skin(private val username: String, private val shouldPrepare: Boolean) {

	private var resourceLocation: ResourceLocation = null
	private var bufferedImage: BufferedImage = null
	private var skin: DynamicTexture = null

	// Default Constructor
	{
		this.resourceLocation = new ResourceLocation("skins/" + this.username)

		if (this.shouldPrepare) {
			this.prepareSkin()
		}

	}

	// End Constructor

	// Other Constructors
	def this(username: String) {
		this(username, false)

	}

	// End Constructors

	def prepareSkin(): Unit = {

		this.skin = Minecraft.getMinecraft().getTextureManager().getTexture(this.resourceLocation)
				.asInstanceOf[DynamicTexture]

		try {
			val url: URL = new URL(
				String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", this.username))

			this.bufferedImage = ImageIO.read(url)
		}
		catch {
			case e: Exception =>
				LogHelper.error(Origin.pluginName, "Invalid " + this.username)
				this.bufferedImage = null
				return
		}

		if (this.skin == null) {
			this.skin = new DynamicTexture(this.bufferedImage.getWidth,
				this.bufferedImage.getHeight)
			Minecraft.getMinecraft.getTextureManager.loadTexture(this.resourceLocation, this.skin)
		}

		this.bufferedImage.getRGB(0, 0, this.bufferedImage.getWidth, this.bufferedImage.getHeight,
			this.skin.getTextureData, 0, this.bufferedImage.getWidth)
		this.skin.updateDynamicTexture()

	}

	def getSkin(): ResourceLocation = {
		this.resourceLocation
	}

	def getSkinWidth(): Int = {
		this.bufferedImage.getWidth
	}

	def getSkinHeight(): Int = {
		this.bufferedImage.getHeight
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	object Part extends Enumeration {
		type Part = Value

		val HEAD, BODY, LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG = Value

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	def render(x: Int, y: Int, scale: Float, part: Part.Value,
			partSideDirection: ForgeDirection,
			isArmor: Boolean, skin: Skin): Unit = {
		this.render(x, y, scale, part, partSideDirection.ordinal(), isArmor, skin)
	}

	def render(x: Int, y: Int, scale: Float, part: Part.Value, partSide: Int,
			isArmor: Boolean, skin: Skin): Unit = {

		var uvwh: Array[Double] = null

		if (skin.getSkinHeight() != 64 && part == Part.LEFTARM) {
			uvwh = this.getUVWH(Part.RIGHTARM, isArmor, partSide)
		}
		else if (skin.getSkinHeight() != 64 && part == Part.LEFTLEG) {
			uvwh = this.getUVWH(Part.RIGHTLEG, isArmor, partSide)
		}
		else {
			uvwh = this.getUVWH(part, isArmor, partSide)
		}

		GL11.glPushMatrix()
		GL11.glScalef(scale, scale, 1.0F)
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		GL11.glTranslatef(x.asInstanceOf[Float] / scale, y.asInstanceOf[Float] / scale, 1.0F)
		UtilRender.bindResource(skin.getSkin())
		this.draw(x, y, uvwh(0), uvwh(1), uvwh(2), uvwh(3), skin.getSkinWidth(),
			skin.getSkinHeight())
		GL11.glPopMatrix()

	}

	def getUVWH(part: Part.Value, isArmor: Boolean, side: Int): Array[Double] = {
		val uvwh: Array[Double] = new Array[Double](4)

		val partUV: Array[Double] = this.getPartUV(part, isArmor)
		val sidedUV: Array[Double] = this.getSidedUV(part, side)
		val partBySideWH: Array[Double] = this.getPartWHBySide(part, side)

		uvwh(0) = partUV(0) + sidedUV(0)
		uvwh(1) = partUV(1) + sidedUV(1)
		uvwh(2) = partBySideWH(0)
		uvwh(3) = partBySideWH(1)

		uvwh
	}

	def getPartUV(part: Part.Value, isArmor: Boolean): Array[Double] = {
		var u: Double = 0.0D
		var v: Double = 0.0D

		part match {
			case Part.HEAD =>
				u = 0.0
				v = 0.0
				if (isArmor) {
					u += 32.0
				}
			case Part.BODY =>
				u = 16.0
				v = 16.0
				if (isArmor) {
					v += 16.0
				}
			case Part.LEFTARM =>
				u = 32.0
				v = 48.0
				if (isArmor) {
					u += 16.0
				}
			case Part.RIGHTARM =>
				u = 40.0
				v = 16.0
				if (isArmor) {
					v += 16.0
				}
			case Part.LEFTLEG =>
				u = 16.0
				v = 48.0
				if (isArmor) {
					u -= 16.0
				}
			case Part.RIGHTLEG =>
				u = 0.0
				v = 16.0
				if (isArmor) {
					v += 16.0
				}
			case _ =>
				u = 0.0
				v = 0.0
		}

		Array[Double](u, v)
	}

	def getSidedUV(part: Part.Value, side: Int): Array[Double] = {
		part match {
			case Part.HEAD =>
				getPartUVBySide(8, 8, side)
			case Part.BODY =>
				getPartUVBySide(4, 8, side)
			case _ =>
				getPartUVBySide(4, 4, side)
		}
	}

	def getPartUVBySide(unitSideU: Int, unitSideV: Int, forgeDirectionSide: Int): Array[Double] = {
		var uMult: Int = 0
		var vMult: Int = 0

		forgeDirectionSide match {
			case 0 =>
				uMult = 2
				vMult = 0
			case 1 =>
				uMult = 1
				vMult = 0
			case 2 =>
				uMult = 1
				vMult = 1
			case 3 =>
				uMult = 2
				vMult = 1
			case 4 =>
				uMult = 3
				vMult = 1
			case 5 =>
				uMult = 0
				vMult = 1
			case _ =>
				uMult = 0
				vMult = 0
		}

		Array[Double](unitSideU * uMult, unitSideV * vMult)
	}

	def getPartWHBySide(part: Part.Value, side: Int): Array[Double] = {
		part match {
			case Part.HEAD =>
				getPartWH(8, 8, 8, side)
			case Part.BODY =>
				getPartWH(8, 4, 12, side)
			case _ =>
				getPartWH(4, 4, 12, side)
		}
	}

	def getPartWH(width: Double, length: Double, height: Double,
			forgeDirectionSide: Int): Array[Double] = {
		var w: Double = 0.0
		var h: Double = 0.0

		forgeDirectionSide match {
			case 0 | 1 =>
				w = width
				h = length
			case 2 | 3 | 4 | 5 =>
				w = width
				h = height
			case _ =>
				w = 0.0
				h = 0.0
		}

		Array[Double](w, h)
	}

	def draw(x: Double, y: Double, u: Double, v: Double, w: Double, h: Double, skinW: Float,
			skinH: Float): Unit = {
		this.draw(x, y, 0.0D, u, v, w, h, skinW, skinH)
	}

	def draw(x: Double, y: Double, z: Double, u: Double, v: Double, w: Double, h: Double,
			skinW: Float, skinH: Float): Unit = {
		val scaledSkinW: Float = 1.0F / skinW
		val scaledSkinH: Float = 1.0F / skinH
		val tessellator: Tessellator = Tessellator.instance

		tessellator.startDrawingQuads()

		tessellator.addVertexWithUV(
			x,
			y + h,
			z,
			u * scaledSkinW.asInstanceOf[Double],
			(v + h) * scaledSkinH.asInstanceOf[Double]
		)
		tessellator.addVertexWithUV(
			x + w,
			y + h,
			z,
			(u + w) * scaledSkinW.asInstanceOf[Double],
			(v + h) * scaledSkinH.asInstanceOf[Double]
		)
		tessellator.addVertexWithUV(
			x + w,
			y,
			z,
			(u + w) * scaledSkinW.asInstanceOf[Double],
			v * scaledSkinH.asInstanceOf[Double]
		)
		tessellator.addVertexWithUV(
			x,
			y,
			z,
			u * scaledSkinW.asInstanceOf[Double],
			v * scaledSkinH.asInstanceOf[Double]
		)

		tessellator.draw()

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	def render3D(horizontal: Double, vertical: Double, scale: Float, part: Part.Value,
			partSide: ForgeDirection, isArmor: Boolean, skin: Skin,
			directionFacing: ForgeDirection): Unit = {
		this.render3D(horizontal, vertical, scale, part, partSide.ordinal(), isArmor, skin,
			directionFacing)
	}

	def render3D(horizontal: Double, vertical: Double, scale: Float, part: Part.Value,
			partSide: Int, isArmor: Boolean, skin: Skin,
			directionFacing: ForgeDirection): Unit = {
		var uvwh: Array[Double] = null

		if (skin.getSkinHeight() != 64 && part == Part.LEFTARM) {
			uvwh = this.getUVWH(Part.RIGHTARM, isArmor, partSide)
		}
		else if (skin.getSkinHeight() != 64 && part == Part.LEFTLEG) {
			uvwh = this.getUVWH(Part.RIGHTLEG, isArmor, partSide)
		}
		else {
			uvwh = this.getUVWH(part, isArmor, partSide)
		}

		GL11.glPushMatrix()
		GL11.glScalef(scale, scale, scale)
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		//GL11.glTranslatef(horizontal.asInstanceOf[Float],
		//		vertical.asInstanceOf[Float], 0.0F
		UtilRender.bindResource(skin.getSkin())
		this.draw3D(horizontal, vertical, uvwh(0), uvwh(1), uvwh(2), uvwh(3), skin.getSkinWidth(),
			skin.getSkinHeight(), directionFacing)
		GL11.glPopMatrix()
	}

	def draw3D(horizontal: Double, vertical: Double, u: Double, v: Double, w: Double, h: Double,
			skinW: Float, skinH: Float, directionFacing: ForgeDirection): Unit = {
		//System.out.println("draw")
		var xyz: Array[Double] = null

		val scaledSkinW: Float = 1.0F / skinW
		val scaledSkinH: Float = 1.0F / skinH
		val tessellator: Tessellator = Tessellator.instance

		tessellator.startDrawingQuads()

		xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 1)
		tessellator.addVertexWithUV(
			xyz(0),
			xyz(1),
			xyz(2),
			u * scaledSkinW.asInstanceOf[Double],
			(v + h) * scaledSkinH.asInstanceOf[Double]
		)
		xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 2)
		tessellator.addVertexWithUV(
			xyz(0),
			xyz(1),
			xyz(2),
			(u + w) * scaledSkinW.asInstanceOf[Double],
			(v + h) * scaledSkinH.asInstanceOf[Double]
		)
		xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 3)
		tessellator.addVertexWithUV(
			xyz(0),
			xyz(1),
			xyz(2),
			(u + w) * scaledSkinW.asInstanceOf[Double],
			v * scaledSkinH.asInstanceOf[Double]
		)
		xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 4)
		tessellator.addVertexWithUV(
			xyz(0),
			xyz(1),
			xyz(2),
			u * scaledSkinW.asInstanceOf[Double],
			v * scaledSkinH.asInstanceOf[Double]
		)


		tessellator.draw()
	}

	def getXYZRenderCoordsFromDirection(horizontal: Double, vertical: Double, width: Double,
			height: Double,
			facingDirection: ForgeDirection, corner: Int): Array[Double] = {
		var x: Double = 0.0
		var y: Double = 0.0
		var z: Double = 0.0

		facingDirection match {
			case ForgeDirection.DOWN => // on y axis, going -y
				corner match {
					case 1 =>
						x = horizontal + width
						z = vertical + height
					case 2 =>
						x = horizontal
						z = vertical + height
					case 3 =>
						x = horizontal
						z = vertical
					case 4 =>
						x = horizontal + width
						z = vertical
				}
				y = 0.0
			case ForgeDirection.UP => // on y axis, going +y
				corner match {
					case 1 =>
						x = horizontal
						z = vertical + height
					case 2 =>
						x = horizontal + width
						z = vertical + height
					case 3 =>
						x = horizontal + width
						z = vertical
					case 4 =>
						x = horizontal
						z = vertical
				}
				y = 0.0
			case ForgeDirection.NORTH => // on z axis, going -z
				corner match {
					case 1 =>
						x = horizontal - width
						y = vertical + height
					case 2 =>
						x = horizontal
						y = vertical + height
					case 3 =>
						x = horizontal
						y = vertical
					case 4 =>
						x = horizontal - width
						y = vertical
				}
				z = 0.0
			case ForgeDirection.SOUTH => // on z axis, going +z
				corner match {
					case 1 =>
						x = horizontal + width
						y = vertical + height
					case 2 =>
						x = horizontal
						y = vertical + height
					case 3 =>
						x = horizontal
						y = vertical
					case 4 =>
						x = horizontal + width
						y = vertical
				}
				z = 0.0
			case ForgeDirection.WEST => // on x axis, going -x
				corner match {
					case 1 =>
						z = horizontal + width
						y = vertical + height
					case 2 =>
						z = horizontal
						y = vertical + height
					case 3 =>
						z = horizontal
						y = vertical
					case 4 =>
						z = horizontal + width
						y = vertical
				}
				x = 0.0
			case ForgeDirection.EAST => // on x axis, going +x
				corner match {
					case 1 =>
						z = horizontal - width
						y = vertical + height
					case 2 =>
						z = horizontal
						y = vertical + height
					case 3 =>
						z = horizontal
						y = vertical
					case 4 =>
						z = horizontal - width
						y = vertical
				}
				x = 0.0
			case _ =>

		}

		Array[Double](x, y, z)
	}

	/* Drawing a head


		GL11.glPushMatrix()
		// translated for the te
		GL11.glTranslatef(0.0F, -0.5F, 0.0F)

		val scale: Float = 0.1F
		GL11.glTranslatef(0.0F, 3.0F, 0.0F)

		GL11.glScalef(scale, scale, scale)

		val skin: Skin = new util.Skin("Country_Gamer", true)

		skin.render3D(0.0D, 0.0D, 1.0F, skin.Part.HEAD, ForgeDirection.NORTH, false, skin, ForgeDirection.NORTH)
		skin.render3D(0.0D, 0.0D, 1.0F, skin.Part.HEAD, ForgeDirection.EAST, false, skin, ForgeDirection.EAST)
		skin.render3D(0.0D, 0.0D, 1.0F, skin.Part.HEAD, ForgeDirection.SOUTH, false, skin, ForgeDirection.SOUTH)
		skin.render3D(0.0D, 0.0D, 1.0F, skin.Part.HEAD, ForgeDirection.WEST, false, skin, ForgeDirection.WEST)

		GL11.glPopMatrix()
	 */

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * @author maxpowa (permissions given)
	 */
	/*
		import java.awt.image.BufferedImage
		import java.net.URL

		import javax.imageio.ImageIO

		import net.minecraft.client.Minecraft
		import net.minecraft.client.renderer.texture.DynamicTexture
		import net.minecraft.util.ResourceLocation

		public class SkinHolder {

			private BufferedImage bufferedimage
			private String username
			private ResourceLocation resourceLocation
			private DynamicTexture skin

			public SkinHolder(String username) {
				this.username = username
				this.resourceLocation = new ResourceLocation("skins/"+username)
				this.prepareSkin()
			}

			public void prepareSkin() {
				this.skin = (DynamicTexture)Minecraft.getMinecraft().getTextureManager().getTexture(this.resourceLocation)

				try
				{
					URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png",this.username))
					this.bufferedimage = ImageIO.read(url)
				}
				catch (Exception exception)
				{
					// Maybe log? "Invalid icon for "+this.username
					this.bufferedimage = null
					return
				}

				if (this.skin == null)
				{
					this.skin = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight())
					Minecraft.getMinecraft().getTextureManager().loadTexture(this.resourceLocation, this.skin)
				}

				bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.skin.getTextureData(), 0, bufferedimage.getWidth())
				this.skin.updateDynamicTexture()
			}

			public ResourceLocation getResourceLocation() {
				return this.resourceLocation
			}

		}
	 */

}