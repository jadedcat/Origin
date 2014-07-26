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
		this(username, true)

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

	object Part0 extends Enumeration {
		type Part = Value

		val HEAD, BODY, LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG = Value

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	def render(x: Int, y: Int, scale: Float, part: Part0.Value,
			partSideDirection: ForgeDirection,
			isArmor: Boolean, skin: Skin): Unit = {
		this.render(x, y, scale, part, partSideDirection.ordinal(), isArmor, skin)
	}

	def render(x: Int, y: Int, scale: Float, part: Part0.Value, partSide: Int,
			isArmor: Boolean, skin: Skin): Unit = {

		var uvwh: Array[Double] = null

		if (skin.getSkinHeight() != 64 && part == Part0.LEFTARM) {
			uvwh = this.getUVWH(Part0.RIGHTARM, isArmor, partSide)
		}
		else if (skin.getSkinHeight() != 64 && part == Part0.LEFTLEG) {
			uvwh = this.getUVWH(Part0.RIGHTLEG, isArmor, partSide)
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

	def getUVWH(part: Part0.Value, isArmor: Boolean, side: Int): Array[Double] = {
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

	def getPartUV(part: Part0.Value, isArmor: Boolean): Array[Double] = {
		var u: Double = 0.0D
		var v: Double = 0.0D

		part match {
			case Part0.HEAD =>
				u = 0.0
				v = 0.0
				if (isArmor) {
					u += 32.0
				}
			case Part0.BODY =>
				u = 16.0
				v = 16.0
				if (isArmor) {
					v += 16.0
				}
			case Part0.LEFTARM =>
				u = 32.0
				v = 48.0
				if (isArmor) {
					u += 16.0
				}
			case Part0.RIGHTARM =>
				u = 40.0
				v = 16.0
				if (isArmor) {
					v += 16.0
				}
			case Part0.LEFTLEG =>
				u = 16.0
				v = 48.0
				if (isArmor) {
					u -= 16.0
				}
			case Part0.RIGHTLEG =>
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

	def getSidedUV(part: Part0.Value, side: Int): Array[Double] = {
		part match {
			case Part0.HEAD =>
				getPartUVBySide(8, 8, side)
			case Part0.BODY =>
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

	def getPartWHBySide(part: Part0.Value, side: Int): Array[Double] = {
		part match {
			case Part0.HEAD =>
				getPartWH(8, 8, 8, side)
			case Part0.BODY =>
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
	*/
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
def render3D(horizontal: Double, vertical: Double, scale: Float, part: Part0.Value,
		partSide: ForgeDirection, isArmor: Boolean, skin: Skin,
		directionFacing: ForgeDirection): Unit = {
	this.render3D(horizontal, vertical, scale, part, partSide.ordinal(), isArmor, skin,
		directionFacing)
}

def render3D(horizontal: Double, vertical: Double, scale: Float, part: Part0.Value,
		partSide: Int, isArmor: Boolean, skin: Skin,
		directionFacing: ForgeDirection): Unit = {
	var uvwh: Array[Double] = null

	if (skin.getSkinHeight() != 64 && part == Part0.LEFTARM) {
		uvwh = this.getUVWH(Part0.RIGHTARM, isArmor, partSide)
	}
	else if (skin.getSkinHeight() != 64 && part == Part0.LEFTLEG) {
		uvwh = this.getUVWH(Part0.RIGHTLEG, isArmor, partSide)
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

def renderPart(hor: Double, ver: Double, scale: Float, part: Part0.Value, partSide: Int,
		isArmor: Boolean, skin: Skin, directionFacing: ForgeDirection): Unit = {
	// Create a variable for the u, v, width, and height of the rendering image
	var uvwh: Array[Double] = null

	// Check to make sure, if the image does not have areas for separate parts
	// (say the image is of the old type when arms were the same and legs were the same).
	// If the image is of the old style, and the parts are special, then render the passed parts
	// Using the old system
	if (skin.getSkinHeight() != 64 && part == Part0.LEFTARM) {
		uvwh = this.getUVWH(Part0.RIGHTARM, isArmor, partSide)
	}
	else if (skin.getSkinHeight() != 64 && part == Part0.LEFTLEG) {
		uvwh = this.getUVWH(Part0.RIGHTLEG, isArmor, partSide)
	}
	else {
		uvwh = this.getUVWH(part, isArmor, partSide)
	}

	// New matrix for rendering
	GL11.glPushMatrix()
	// Make sure render is in full color
	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
	// Scale the image to the user's whim
	GL11.glScalef(scale, scale, scale)
	// Bind the texture
	UtilRender.bindResource(skin.getSkin())

	var x: Double = 0.0
	var y: Double = 0.0
	var z: Double = 0.0
	directionFacing match {
		case ForgeDirection.DOWN | ForgeDirection.UP =>
			x = hor
			y = 0.0
			z = ver
		case ForgeDirection.NORTH | ForgeDirection.SOUTH =>
			x = hor
			y = ver
			z = 0.0
		case ForgeDirection.WEST | ForgeDirection.EAST =>
			x = 0.0
			y = ver
			z = hor
		case _ =>
			x = 0.0
			y = 0.0
			z = 0.0
	}

	GL11.glTranslated(x, y, z)

	// Time to do the render code!
	this.drawPart(hor, ver, uvwh(0), uvwh(1), uvwh(2), uvwh(3), skin.getSkinWidth(),
		skin.getSkinHeight(), directionFacing)

	// End the matrix
	GL11.glPopMatrix()

}

def drawPart(horizontal: Double, vertical: Double, u: Double, v: Double, w: Double, h: Double,
		skinW: Float, skinH: Float, directionFacing: ForgeDirection): Unit = {
	var xyz: Array[Double] = null

	val scaledSkinW: Float = 1.0F / skinW
	val scaledSkinH: Float = 1.0F / skinH
	val tess: Tessellator = Tessellator.instance

	tess.startDrawingQuads()

	xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 1)
	tess.addVertexWithUV(0.0D, 0.0D, 0.0D,
		u * scaledSkinW.asInstanceOf[Double], (v + h) * scaledSkinH.asInstanceOf[Double])

	xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 2)
	tess.addVertexWithUV(0.0D, 0.0D, 0.0D,
		(u + w) * scaledSkinW.asInstanceOf[Double], (v + h) * scaledSkinH.asInstanceOf[Double])

	xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 3)
	tess.addVertexWithUV(0.0D, 0.0D, 0.0D,
		(u + w) * scaledSkinW.asInstanceOf[Double], v * scaledSkinH.asInstanceOf[Double])


	xyz = this.getXYZRenderCoordsFromDirection(horizontal, vertical, w, h, directionFacing, 4)
	tess.addVertexWithUV(0.0D, 0.0D, 0.0D,
		u * scaledSkinW.asInstanceOf[Double], v * scaledSkinH.asInstanceOf[Double])

	tess.draw()

}

def getXYZForCorner(width: Double, height: Double, facingDirection: ForgeDirection,
		corner: Int): Array[Double] = {
	var x: Double = 0.0
	var y: Double = 0.0
	var z: Double = 0.0

	// TODO
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
	*/

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

	final object PartType extends Enumeration {
		type PartType = Value

		val HEAD, BODY, LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG = Value

	}

	class Part(private var partType: PartType.Value, private val armor: Boolean) {

		def getType(): PartType.Value = {
			this.partType
		}

		def setType(part: PartType.Value): Unit = {
			this.partType = part
		}

		def isArmor(): Boolean = {
			this.armor
		}

		def toSided(side: ForgeDirection): SidedPart = {
			new SidedPart(this.partType, this.armor, side)
		}

	}

	class SidedPart(partType: PartType.Value, armor: Boolean, private val side: Int)
			extends Part(partType, armor) {

		def this(partType: PartType.Value, armor: Boolean, side: ForgeDirection) {
			this(partType, armor, side.ordinal())

		}

		def getSide(): Int = {
			this.side
		}

		def getDirection(): ForgeDirection = {
			ForgeDirection.getOrientation(this.getSide())
		}

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/* Statue Render Code

GL11.glPushMatrix()
		GL11.glTranslatef(0.0F, -0.5F, 0.0F)
		val skin: Skin = new Skin("FireBall1725")
		/*
		var part: skin.Part = new skin.Part(skin.PartType.HEAD, false)
		skin.render(0.0D, 8.0D, 8.0D, 8.0D, 8.0D, part)
		part = new skin.Part(skin.PartType.BODY, false)
		skin.render(0.0D, 0.0D, 8.0D, 12.0D, 4.0D, part)
		part = new skin.Part(skin.PartType.RIGHTARM, false)
		skin.render(6.0D, 0.0D, 4.0D, 12.0D, 4.0D, part)
		part = new skin.Part(skin.PartType.LEFTARM, false)
		skin.render(-6.0D, 0.0D, 4.0D, 12.0D, 4.0D, part)
		part = new skin.Part(skin.PartType.RIGHTLEG, false)
		skin.render(-2.0D, -12.0D, 4.0D, 12.0D, 4.0D, part)
		part = new skin.Part(skin.PartType.LEFTLEG, false)
		skin.render(2.0D, -12.0D, 4.0D, 12.0D, 4.0D, part)
		*/
		skin.render(1.0D, 0.0D)

		GL11.glPopMatrix()

	*/

	def render(horizontal: Double, vertical: Double): Unit = {
		this.render(horizontal, vertical, false)
		// TODO fix armor layer
		//GL11.glScalef(1.1F, 1.1F, 1.1F)
		//this.render(horizontal, vertical, true)

	}

	def render(horizontal: Double, vertical: Double, isArmorLayer: Boolean): Unit = {
		this.render(1.0D, 0.0D, 0.475D, isArmorLayer)
	}

	def render(horizontal: Double, vertical: Double, headSize: Double,
			isArmorLayer: Boolean): Unit = {

		val scale: Double = headSize / 8.0D

		val eight: Double = 8.0D * scale
		val four: Double = 4.0D * scale
		val twelve: Double = 12.0D * scale

		this.render(
			horizontal - (2.0D * scale),
			vertical + (12.0D * scale),
			four, twelve, four, new Part(PartType.RIGHTLEG, isArmorLayer))
		this.render(
			horizontal + (2.0D * scale),
			vertical + (12.0D * scale),
			four, twelve, four, new Part(PartType.LEFTLEG, isArmorLayer))
		this.render(
			horizontal,
			vertical + (24.0D * scale),
			eight, twelve, four, new Part(PartType.BODY, isArmorLayer))
		this.render(
			horizontal + (6.0D * scale),
			vertical + (24.0D * scale),
			four, twelve, four, new Part(PartType.RIGHTARM, isArmorLayer))
		this.render(
			horizontal - (6.0D * scale),
			vertical + (24.0D * scale),
			four, twelve, four, new Part(PartType.LEFTARM, isArmorLayer))
		this.render(
			horizontal,
			vertical + (32.0D * scale),
			eight, eight, eight, new Part(PartType.HEAD, isArmorLayer))

	}

	def render(horizontal: Double, vertical: Double, requestedWidth: Double,
			requestedHeight: Double, requestedLength: Double, part: Part): Unit = {

		GL11.glPushMatrix()

		GL11.glTranslated(horizontal, 0.0D, 0.0D)

		GL11.glTranslated(-(requestedWidth / 2), vertical, requestedLength / 2)

		// Top
		this.render(
			0.0D,
			0.0D,
			requestedWidth, requestedLength, part.toSided(ForgeDirection.UP),
			ForgeDirection.UP
		)

		GL11.glTranslated(0.0D, -requestedHeight, 0.0D)
		// Bottom
		this.render(
			0.0D,
			0.0D,
			requestedWidth, requestedLength, part.toSided(ForgeDirection.DOWN),
			ForgeDirection.DOWN
		)
		GL11.glTranslated(0.0D, requestedHeight, 0.0D)

		GL11.glTranslated(requestedWidth, 0.0D, -requestedLength)
		// Front
		this.render(
			0.0D,
			0.0D,
			requestedWidth, requestedHeight, part.toSided(ForgeDirection.NORTH),
			ForgeDirection.NORTH
		)
		GL11.glTranslated(-requestedWidth, 0.0D, requestedLength)

		// Back
		///*
		this.render(
			0.0D,
			0.0D,
			requestedWidth, requestedHeight, part.toSided(ForgeDirection.SOUTH),
			ForgeDirection.SOUTH
		)
		//*/

		GL11.glTranslated(0.0D, 0.0D, -requestedLength)
		// Right
		///*
		this.render(
			0.0D,
			0.0D,
			requestedLength, requestedHeight, part.toSided(ForgeDirection.WEST),
			ForgeDirection.WEST
		)
		//*/
		GL11.glTranslated(0.0D, 0.0D, requestedLength)

		GL11.glTranslated(requestedWidth, 0.0D, 0.0D)
		// Left
		///*
		this.render(
			0.0D,
			0.0D,
			requestedLength, requestedHeight, part.toSided(ForgeDirection.EAST),
			ForgeDirection.EAST
		)
		//*/
		GL11.glTranslated(-requestedWidth, 0.0D, 0.0D)

		GL11.glTranslated(+(requestedWidth / 2), -vertical, -(requestedLength / 2))
		GL11.glPopMatrix()

	}

	def render(horizontal: Double, vertical: Double, requestedWidth: Double,
			requestedHeight: Double, part: SidedPart, facingDirection: ForgeDirection): Unit = {

		if (part.isArmor() && this.getSkinHeight() != 64 && part.getType() != PartType.HEAD) {
			return
		}

		if (this.getSkinHeight() != 64 && part.getType() == PartType.LEFTARM) {
			part.setType(PartType.RIGHTARM)
		}
		else if (this.getSkinHeight() != 64 && part.getType() == PartType.LEFTLEG) {
			part.setType(PartType.RIGHTLEG)
		}

		val xyz: Array[Double] = this.getXYZByDirection(horizontal, vertical, facingDirection)
		val uvwh: Array[Double] = this.getUVWH(part)

		GL11.glPushMatrix()
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		GL11.glTranslated(xyz(0), xyz(1), xyz(2))
		UtilRender.bindResource(this.getSkin())

		val scaleXYZ: Array[Double] = this
				.getXYZByDirection(requestedWidth / uvwh(2), requestedHeight / uvwh(3),
		            facingDirection)
		GL11.glScaled(scaleXYZ(0), scaleXYZ(1), scaleXYZ(2))

		this.draw(uvwh(0), uvwh(1), uvwh(2), uvwh(3), this.getSkinWidth(), this.getSkinHeight(),
			facingDirection)

		GL11.glScaled(1.0D / scaleXYZ(0), 1.0D / scaleXYZ(1), 1.0D / scaleXYZ(2))
		GL11.glTranslated(-xyz(0), -xyz(1), -xyz(2))
		GL11.glPopMatrix()

	}

	def draw(u: Double, v: Double, uwidth: Double, vheight: Double, skinWidth: Float,
			skinHeight: Float, direction: ForgeDirection): Unit = {

		val scaledSkinWidth: Float = 1.0F / skinWidth
		val scaledSkinHeight: Float = 1.0F / skinHeight
		val tessellator: Tessellator = Tessellator.instance

		tessellator.startDrawingQuads

		/*
		tessellator.addVertexWithUV(
			x.asInstanceOf[Double],
			(y + height).asInstanceOf[Double],
			0.0D,
			(u * f4).asInstanceOf[Double],
			((v + height.asInstanceOf[Float]) * scaledSkinHeight).asInstanceOf[Double])


		tessellator.addVertexWithUV(
			(x + width).asInstanceOf[Double],
			(y + height).asInstanceOf[Double],
			0.0D,
			((u + width.asInstanceOf[Float]) * scaledSkinWidth).asInstanceOf[Double],
			((v + height.asInstanceOf[Float]) * scaledSkinHeight).asInstanceOf[Double])


		tessellator.addVertexWithUV(
			(x + width).asInstanceOf[Double],
			y.asInstanceOf[Double],
			0.0D,
			((u + width.asInstanceOf[Float]) * scaledSkinWidth).asInstanceOf[Double],
			(v * scaledSkinHeight).asInstanceOf[Double])


		tessellator.addVertexWithUV(
			x.asInstanceOf[Double],
			y.asInstanceOf[Double],
			0.0D,
			(u * scaledSkinWidth).asInstanceOf[Double],
			(v * scaledSkinHeight).asInstanceOf[Double])
		*/

		var wlh: Array[Double] = null

		wlh = this.getXYZByDirectionAndCorner(uwidth, vheight, direction, 1)
		tessellator.addVertexWithUV(
			wlh(0),
			wlh(1),
			wlh(2),
			u * scaledSkinWidth,
			(v + vheight) * scaledSkinHeight
		)

		wlh = this.getXYZByDirectionAndCorner(uwidth, vheight, direction, 2)
		tessellator.addVertexWithUV(
			wlh(0),
			wlh(1),
			wlh(2),
			(u + uwidth) * scaledSkinWidth,
			(v + vheight) * scaledSkinHeight
		)

		wlh = this.getXYZByDirectionAndCorner(uwidth, vheight, direction, 3)
		tessellator.addVertexWithUV(
			wlh(0),
			wlh(1),
			wlh(2),
			(u + uwidth) * scaledSkinWidth,
			v * scaledSkinHeight
		)

		wlh = this.getXYZByDirectionAndCorner(uwidth, vheight, direction, 4)
		tessellator.addVertexWithUV(
			wlh(0),
			wlh(1),
			wlh(2),
			u * scaledSkinWidth,
			v * scaledSkinHeight
		)

		tessellator.draw
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	def getXYZByDirection(horizontal: Double, vertical: Double,
			direction: ForgeDirection): Array[Double] = {
		this.getXYZByDirectionAndCorner(horizontal, vertical, direction, 0)
	}

	def getXYZByDirectionAndCorner(horizontal: Double, vertical: Double,
			direction: ForgeDirection, corner: Int): Array[Double] = {
		var x: Double = 0.0
		var y: Double = 0.0
		var z: Double = 0.0

		direction match {
			case ForgeDirection.DOWN => // on y axis, going -y
				y = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							x = 0.0
							z = 0.0 - vertical
						case 2 =>
							x = 0.0 + horizontal
							z = 0.0 - vertical
						case 3 =>
							x = 0.0 + horizontal
							z = 0.0
						case 4 =>
							x = 0.0
							z = 0.0
					}
				}
				else {
					x = horizontal
					z = vertical
				}
			case ForgeDirection.UP => // on y axis, going +y
				y = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							x = 0.0 + horizontal
							z = 0.0 - vertical
						case 2 =>
							x = 0.0
							z = 0.0 - vertical
						case 3 =>
							x = 0.0
							z = 0.0
						case 4 =>
							x = 0.0 + horizontal
							z = 0.0
					}
				}
				else {
					x = horizontal
					z = vertical
				}
			case ForgeDirection.NORTH => // on z axis, going -z
				z = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							x = 0.0
							y = 0.0 - vertical
						case 2 =>
							x = 0.0 - horizontal
							y = 0.0 - vertical
						case 3 =>
							x = 0.0 - horizontal
							y = 0.0
						case 4 =>
							x = 0.0
							y = 0.0
					}
				}
				else {
					x = horizontal
					y = vertical
				}
			case ForgeDirection.SOUTH => // on z axis, going +z
				z = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							x = 0.0
							y = 0.0 - vertical
						case 2 =>
							x = 0.0 + horizontal
							y = 0.0 - vertical
						case 3 =>
							x = 0.0 + horizontal
							y = 0.0
						case 4 =>
							x = 0.0
							y = 0.0
					}
				}
				else {
					x = horizontal
					y = vertical
				}
			case ForgeDirection.WEST => // on x axis, going -x
				x = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							z = 0.0
							y = 0.0 - vertical
						case 2 =>
							z = 0.0 + horizontal
							y = 0.0 - vertical
						case 3 =>
							z = 0.0 + horizontal
							y = 0.0
						case 4 =>
							z = 0.0
							y = 0.0
					}
				}
				else {
					z = horizontal
					y = vertical
				}
			case ForgeDirection.EAST => // on x axis, going +x
				x = 0.0
				if (corner > 0) {
					corner match {
						case 1 =>
							z = 0.0
							y = 0.0 - vertical
						case 2 =>
							z = 0.0 - horizontal
							y = 0.0 - vertical
						case 3 =>
							z = 0.0 - horizontal
							y = 0.0
						case 4 =>
							z = 0.0
							y = 0.0
					}
				}
				else {
					z = horizontal
					y = vertical
				}
			case _ =>

		}

		Array[Double](x, y, z)
	}

	def getUVWH(part: SidedPart): Array[Double] = {
		val uvwh: Array[Double] = new Array[Double](4)

		val partUV: Array[Double] = this.getPartUV(part)
		val sidedUV: Array[Double] = this.getSidedUV(part)
		val partBySideWH: Array[Double] = this.getPartWHBySide(part)

		uvwh(0) = partUV(0) + sidedUV(0)
		uvwh(1) = partUV(1) + sidedUV(1)
		uvwh(2) = partBySideWH(0)
		uvwh(3) = partBySideWH(1)

		uvwh
	}

	def getPartUV(part: SidedPart): Array[Double] = {
		var u: Double = 0.0D
		var v: Double = 0.0D

		part.getType() match {
			case PartType.HEAD =>
				u = 0.0
				v = 0.0
				if (part.isArmor()) {
					u += 32.0
				}
			case PartType.BODY =>
				u = 16.0
				v = 16.0
				if (part.isArmor()) {
					v += 16.0
				}
			case PartType.LEFTARM =>
				u = 32.0
				v = 48.0
				if (part.isArmor()) {
					u += 16.0
				}
			case PartType.RIGHTARM =>
				u = 40.0
				v = 16.0
				if (part.isArmor()) {
					v += 16.0
				}
			case PartType.LEFTLEG =>
				u = 16.0
				v = 48.0
				if (part.isArmor()) {
					u -= 16.0
				}
			case PartType.RIGHTLEG =>
				u = 0.0
				v = 16.0
				if (part.isArmor()) {
					v += 16.0
				}
			case _ =>
				u = 0.0
				v = 0.0
		}

		Array[Double](u, v)
	}

	def getSidedUV(part: SidedPart): Array[Double] = {
		part.getType() match {
			case PartType.HEAD =>
				this.getPartUVBySide(8, 8, part.getSide())
			case PartType.BODY =>
				this.getPartUVBySide(8, 4, part.getSide())
			case _ =>
				this.getPartUVBySide(4, 4, part.getSide())
		}
	}

	def getPartUVBySide(unitSideU: Int, unitSideV: Int, forgeDirectionSide: Int): Array[Double] = {
		var uMult: Int = 0
		var vMult: Int = 0

		forgeDirectionSide match {
			case 0 =>
				uMult = (unitSideV * 1) + (unitSideU * 1)
				vMult = 0
			case 1 =>
				uMult = unitSideV * 1
				vMult = 0
			case 2 =>
				uMult = unitSideV * 1
				vMult = unitSideV * 1
			case 3 =>
				uMult = (unitSideV * 1) + (unitSideU * 1)
				vMult = unitSideV * 1
			case 4 =>
				uMult = (unitSideV * 1) + (unitSideU * 2)
				vMult = unitSideV * 1
			case 5 =>
				uMult = 0
				vMult = unitSideV * 1
			case _ =>
				uMult = 0
				vMult = 0
		}

		Array[Double](uMult, vMult)
	}

	def getPartWHBySide(part: SidedPart): Array[Double] = {
		(part.getType()) match {
			case PartType.HEAD =>
				this.getPartWH(8, 8, 8, part.getSide())
			case PartType.BODY =>
				this.getPartWH(8, 4, 12, part.getSide())
			case _ =>
				this.getPartWH(4, 4, 12, part.getSide())
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
			case 2 | 3 =>
				w = width
				h = height
			case 4 | 5 =>
				w = length
				h = height
			case _ =>
				w = 0.0
				h = 0.0
		}

		Array[Double](w, h)
	}

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

		// Sample renderer
		GL11.glPushMatrix();
        GL11.glScalef(scale,  scale, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(x/scale, y/scale, 1.0f);
        mc.getTextureManager().bindTexture(user.getSkin());
        Gui.func_146110_a(0, 0, 8.0F, 8.0F, 8, 8, user.getSkinWidth(), user.getSkinHeight());
        Gui.func_146110_a(0, 0, 40.0F, 8.0F, 8, 8, user.getSkinWidth(), user.getSkinHeight());
        GL11.glPopMatrix();
	 */

}
