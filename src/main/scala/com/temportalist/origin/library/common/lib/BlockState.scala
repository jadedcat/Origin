package com.temportalist.origin.library.common.lib

import com.temportalist.origin.library.common.lib.vec.BlockPos
import net.minecraft.block.Block
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist 3/29/15
 */
class BlockState(private var block: Block, private var meta: Int) {

	def getBlock(): Block = this.block

	def setBlock(b: Block): Unit = this.block = b

	def getMeta(): Int = this.meta

	def setMeta(m: Int): Unit = this.meta = m

	def setInWorld(world: World, pos: BlockPos): Unit =
		pos.setBlock(world, this.block, this.meta, 3)

}
