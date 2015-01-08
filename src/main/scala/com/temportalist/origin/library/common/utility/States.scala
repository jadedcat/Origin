package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.NameParser
import net.minecraft.block.state.IBlockState
import net.minecraft.block.Block
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author TheTemportalist
 */
object States {

	def getNameFromState(state: IBlockState): String = {
		val block: Block = state.getBlock
		NameParser.getName(
			new ItemStack(block, 1, block.getMetaFromState(state)), hasID = true, hasMeta = true
		)
	}

	def getStateFromName(name: String): IBlockState = {
		val camoStack: ItemStack = NameParser.getItemStack(name)
		if (camoStack != null && camoStack.getItem != null) {
			val block: Block = Block.getBlockFromItem(camoStack.getItem)
			if (block != null) {
				return block.getStateFromMeta(camoStack.getMetadata)
			}
		}
		null
	}

}
