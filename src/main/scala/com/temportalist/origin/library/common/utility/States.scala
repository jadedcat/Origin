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

	def toState(stack: ItemStack): IBlockState = {
		if (this.isBlock(stack.getItem))
			Block.getBlockFromItem(stack.getItem).getStateFromMeta(stack.getMetadata)
		else null
	}

	def toStack(state: IBlockState): ItemStack = {
		val stack: ItemStack = new ItemStack(
			state.getBlock, 1, state.getBlock.getMetaFromState(state)
		)
		/* todo find a decent way to save the tag properly
		state match {
			case extended: IExtendedBlockState =>
				val tag: NBTTagCompound = new NBTTagCompound
				val unlisteds = extended.getUnlistedProperties
				for (entry <- JavaConversions.asScalaIterator(unlisteds.entrySet().iterator())) {
					val prop: IUnlistedProperty[_] = entry.getKey
					val opt: Optional[_] = entry.getValue

				}
				stack.setTagCompound(tag)
			case _ =>
		}
		*/
		stack
	}

	def getName(state: IBlockState): String = NameParser.getName(state, true, true)

	def getState(name: String): IBlockState = NameParser.getState(name)

}
