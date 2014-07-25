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

	/*
	object Part extends Enumeration {
		type Part = Value

		val HEAD, BODY, LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG = Value

	}
	*/

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	def render(x: Int, y: Int, scale: Float, part: SkinPart.Value,
			partSideDirection: ForgeDirection,
			isArmor: Boolean, skin: Skin): Unit = {
		this.render(x, y, scale, part, partSideDirection.ordinal(), isArmor, skin)
	}

	def render(x: Int, y: Int, scale: Float, part: SkinPart.Value, partSide: Int,
			isArmor: Boolean, skin: Skin): Unit = {

	}

	def getUVWH(): Array[Double] = {
		val uvwh: Array[Double] = new Array[Double](4)

		val partUVWH: Array[Double] = null
		val sidedUVWH: Array[Double] = null

		uvwh(0) = partUVWH(0) + sidedUVWH(0)
		uvwh(1) = partUVWH(1) + sidedUVWH(1)
		uvwh(2) = partUVWH(2) + sidedUVWH(2)
		uvwh(3) = partUVWH(3) + sidedUVWH(3)

		uvwh
	}

	def getPartUVWH(part: SkinPart.Value, isArmor: Boolean): Unit = {

	}

	def draw(x: Int, y: Int, u: Float, v: Float, w: Int, h: Int, skinW: Float,
			skinH: Float): Unit = {
		this.draw(x, y, 0, u, v, w, h, skinW, skinH)
	}

	def draw(x: Int, y: Int, z: Int, u: Float, v: Float, w: Int, h: Int, skinW: Float,
			skinH: Float): Unit = {
		val scaledSkinW: Float = 1.0F / skinW
		val scaledSkinH: Float = 1.0F / skinH
		val tessellator: Tessellator = Tessellator.instance

		tessellator.startDrawingQuads()

		tessellator.addVertexWithUV(
			x.asInstanceOf[Double],
			(y + h).asInstanceOf[Double],
			z.asInstanceOf[Double],
			(u * scaledSkinW).asInstanceOf[Double],
			((v + h.asInstanceOf[Float]).asInstanceOf[Double] * scaledSkinH)
		)
		tessellator.addVertexWithUV(
			(x + w).asInstanceOf[Double],
			(y + h).asInstanceOf[Double],
			z.asInstanceOf[Double],
			((u + w.asInstanceOf[Float]).asInstanceOf[Double] * scaledSkinW),
			((v + h.asInstanceOf[Float]).asInstanceOf[Double] * scaledSkinH)
		)
		tessellator.addVertexWithUV(
			(x + w).asInstanceOf[Double],
			y.asInstanceOf[Double],
			z.asInstanceOf[Double],
			((u + w.asInstanceOf[Float]).asInstanceOf[Double] * scaledSkinW),
			(v * scaledSkinH).asInstanceOf[Double]
		)
		tessellator.addVertexWithUV(
			x.asInstanceOf[Double],
			y.asInstanceOf[Double],
			z.asInstanceOf[Double],
			(u * scaledSkinW).asInstanceOf[Double],
			(v * scaledSkinH).asInstanceOf[Double]
		)

		tessellator.draw()

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
	 */

}
