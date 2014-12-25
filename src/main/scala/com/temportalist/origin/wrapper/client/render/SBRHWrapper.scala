package com.temportalist.origin.wrapper.client.render

import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@Deprecated // todo make new wrapper for 1.8's version of SimpleBlockRenderingHandler
@SideOnly(Side.CLIENT)
class SBRHWrapper(final val renderID: Int) {/*extends ISimpleBlockRenderingHandler {

	override def renderInventoryBlock(block: Block, metadata: Int, modelId: Int,
			renderer: RenderBlocks): Unit = {

		val worldRenderer: WorldRenderer = Tessellator.getInstance().getWorldRenderer

		block.setBlockBoundsForItemRender
		renderer.setRenderBoundsFromBlock(block)
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F)

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(0.0F, -1.0F, 0.0F)
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 0, metadata))
		worldRenderer.draw()

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(0.0F, 1.0F, 0.0F)
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 1, metadata))
		worldRenderer.draw()

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(0.0F, 0.0F, -1.0F)
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 2, metadata))
		worldRenderer.draw()

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(0.0F, 0.0F, 1.0F)
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 3, metadata))
		worldRenderer.draw()

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(-1.0F, 0.0F, 0.0F)
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 4, metadata))
		worldRenderer.draw()

		worldRenderer.startDrawingQuads()
		worldRenderer.setNormal(1.0F, 0.0F, 0.0F)
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 5, metadata))
		worldRenderer.draw()

		GL11.glTranslatef(0.5F, 0.5F, 0.5F)
	}

	override def getRenderId: Int = {
		this.renderID
	}

	override def shouldRender3DInInventory(modelId: Int): Boolean = {
		true
	}

	override def renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block,
			modelId: Int, renderer: RenderBlocks): Boolean = {
		this.render(world, x, y, z, block, modelId, renderer)
	}

	def render(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block,
			modelId: Int, renderer: RenderBlocks): Boolean = {
		false
	}

	*/

}
