package com.countrygamer.cgo.wrapper.common.tile

import java.util

import net.minecraft.block.Block
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author CountryGamer
 */
/**
 * Makes it able for tile entities to easily have custom drops
 *
 * @author CountryGamer
 */
trait ICustomDrops {

	def getDrops(drops: util.ArrayList[ItemStack], block: Block, metadata: Int) {
	}

}
