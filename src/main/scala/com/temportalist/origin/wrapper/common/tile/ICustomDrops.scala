package com.temportalist.origin.wrapper.common.tile

import java.util

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
/**
 * Makes it able for tile entities to easily have custom drops
 *
 * @author TheTemportalist
 */
trait ICustomDrops extends TileEntity {

	def getDrops(drops: util.List[ItemStack], block: Block, state: IBlockState) {
	}

}
