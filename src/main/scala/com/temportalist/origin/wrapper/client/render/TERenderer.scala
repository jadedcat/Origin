package com.temportalist.origin.wrapper.client.render

import com.temportalist.origin.library.client.utility.Rendering
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class TERenderer(val texture: ResourceLocation) extends TileEntitySpecialRenderer {

	def this() {
		this(null)
	}

	override def renderTileEntityAt(tileEntity: TileEntity, viewX: Double, viewY: Double,
			viewZ: Double, renderPartialTicks: Float, int: Int): Unit = {
		GlStateManager.pushMatrix()
		GlStateManager.translate(viewX + 0.5, viewY + 0.5, viewZ + 0.5)

		if (this.texture != null)
			Rendering.bindResource(this.texture)

		this.render(tileEntity, renderPartialTicks, 0.0625F)

		GlStateManager.popMatrix()
	}

	protected def render(tileEntity: TileEntity, renderPartialTicks: Float, f5: Float): Unit = {
	}

}
