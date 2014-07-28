package com.countrygamer.cgo.wrapper.client.render

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.{RenderBlocks, Tessellator}
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
class SBRHWrapper(final val renderID: Int) extends ISimpleBlockRenderingHandler {

	override def renderInventoryBlock(block: Block, metadata: Int, modelId: Int,
			renderer: RenderBlocks): Unit = {

		val tessellator: Tessellator = Tessellator.instance
		block.setBlockBoundsForItemRender
		renderer.setRenderBoundsFromBlock(block)
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F)

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, -1.0F, 0.0F)
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 0, metadata))
		tessellator.draw()

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, 1.0F, 0.0F)
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 1, metadata))
		tessellator.draw()

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, 0.0F, -1.0F)
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 2, metadata))
		tessellator.draw()

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, 0.0F, 1.0F)
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 3, metadata))
		tessellator.draw()

		tessellator.startDrawingQuads()
		tessellator.setNormal(-1.0F, 0.0F, 0.0F)
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 4, metadata))
		tessellator.draw()

		tessellator.startDrawingQuads()
		tessellator.setNormal(1.0F, 0.0F, 0.0F)
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 5, metadata))
		tessellator.draw()

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

}
