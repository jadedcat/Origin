package com.countrygamer.cgo.wrapper.client.render

import com.countrygamer.cgo.common.lib.util.UtilRender
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class TERenderer(val texture: ResourceLocation) extends TileEntitySpecialRenderer {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(null)
	}

	// End Constructor
	override def renderTileEntityAt(tileEntity: TileEntity, viewX: Double, viewY: Double,
			viewZ: Double, renderPartialTicks: Float): Unit = {
		GL11.glPushMatrix()

		GL11.glTranslated(viewX + 0.5, viewY + 0.5, viewZ + 0.5)

		UtilRender.bindResource(this.texture)

		this.render(tileEntity, renderPartialTicks, 0.0625F)

		GL11.glPopMatrix()
	}

	protected def render(tileEntity: TileEntity, renderPartialTicks: Float, f5: Float): Unit = {
	}

}
