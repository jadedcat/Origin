package com.countrygamer.cgo.client.render

import com.countrygamer.cgo.common.lib.ItemMeta
import com.countrygamer.cgo.wrapper.common.tile.ICamouflage
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.{Tessellator, RenderBlocks}
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
object BlockCamouflageRender extends ISimpleBlockRenderingHandler {

	final val RENDER_ID: Int = 42025

	override def renderInventoryBlock(block: Block, metadata: Int, modelID: Int,
			renderer: RenderBlocks): Unit = {
		val tessellator: Tessellator = Tessellator.instance
		block.setBlockBoundsForItemRender
		renderer.setRenderBoundsFromBlock(block)
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F)
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F)
		tessellator.startDrawingQuads

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, -1.0F, 0.0F)
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 0, metadata))
		tessellator.draw()

		/*
		if (flag && this.useInventoryTint) {
			k = block.getRenderColor(metadata)
			f2 = (float) (k >> 16 & 255) / 255.0F
			f3 = (float) (k >> 8 & 255) / 255.0F
			float f4 = (float) (k & 255) / 255.0F
			GL11.glColor4f(f2 * p_147800_3_, f3 * p_147800_3_, f4 * p_147800_3_, 1.0F)
		}
		*/

		tessellator.startDrawingQuads()
		tessellator.setNormal(0.0F, 1.0F, 0.0F)
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
			renderer.getBlockIconFromSideAndMetadata(block, 1, metadata))
		tessellator.draw()

		/*
		if (flag && this.useInventoryTint) {
			GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F)
		}
		*/

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

	override def renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block,
			modelId: Int, renderer: RenderBlocks): Boolean = {
		return this.render(world, x, y, z, block, modelId, renderer)
	}

	private def render(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int,
			renderer: RenderBlocks): Boolean = {
		if (modelId == this.getRenderId) {
			val tileEntity: TileEntity = world.getTileEntity(x, y, z)

			if (tileEntity != null && tileEntity.isInstanceOf[ICamouflage]) {
				val camo: ICamouflage = tileEntity.asInstanceOf[ICamouflage]

				if (camo.isCamouflaged()) {
					val itemMeta: ItemMeta = camo.getCamouflage()
					val camoBlock: Block = itemMeta.getBlock

					if (camoBlock != null && camoBlock != Blocks.air) {
						renderer.renderBlockUsingTexture(camoBlock, x, y, z,
							null) //camoBlock.getIcon(0, itemMeta.getMetadata()))
						return true
					}
				}
			}

			renderer.renderStandardBlock(block, x, y, z)
			return true
		}
		return false
	}

	override def shouldRender3DInInventory(p1: Int): Boolean = {
		return true
	}

	override def getRenderId: Int = {
		this.RENDER_ID
	}

}
