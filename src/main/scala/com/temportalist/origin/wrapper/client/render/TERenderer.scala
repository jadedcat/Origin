package com.temportalist.origin.wrapper.client.render

import com.temportalist.origin.library.client.utility.Rendering
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

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
		GL11.glPushMatrix()

		GL11.glTranslated(viewX + 0.5, viewY + 0.5, viewZ + 0.5)

		if (this.texture != null)
			Rendering.bindResource(this.texture)

		this.render(tileEntity, renderPartialTicks, 0.0625F)

		GL11.glPopMatrix()
	}

	protected def render(tileEntity: TileEntity, renderPartialTicks: Float, f5: Float): Unit = {
	}

}
