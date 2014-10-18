package com.temportalist.origin.library.client.render

import com.temportalist.origin.library.common.lib.ItemMeta
import com.temportalist.origin.wrapper.client.render.SBRHWrapper
import com.temportalist.origin.wrapper.common.tile.ICamouflage
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object BlockCamouflageRender extends SBRHWrapper(42025) {

	override def render(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int,
			renderer: RenderBlocks): Boolean = {
		if (modelId == this.getRenderId) {
			val tileEntity: TileEntity = world.getTileEntity(x, y, z)

			if (tileEntity != null && tileEntity.isInstanceOf[ICamouflage]) {
				val camo: ICamouflage = tileEntity.asInstanceOf[ICamouflage]

				if (camo.isCamouflaged) {
					val itemMeta: ItemMeta = camo.getCamouflage
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
		false
	}

}
