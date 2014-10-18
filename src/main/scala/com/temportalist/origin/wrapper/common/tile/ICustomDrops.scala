package com.temportalist.origin.wrapper.common.tile

import java.util

import net.minecraft.block.Block
import net.minecraft.item.ItemStack

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
trait ICustomDrops {

	def getDrops(drops: util.ArrayList[ItemStack], block: Block, metadata: Int) {
	}

}
