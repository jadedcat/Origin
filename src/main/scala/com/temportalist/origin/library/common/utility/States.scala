package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.{BlockState, NameParser}
import net.minecraft.block.Block
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author TheTemportalist
 */
object States {

	// todo move all this into BlockState

	def getState(stack: ItemStack): BlockState = {
		if (WorldHelper.isBlock(stack.getItem))
			new BlockState(Block.getBlockFromItem(stack.getItem), stack.getItemDamage)
		else null
	}

	def getStack(state: BlockState): ItemStack = {
		val stack: ItemStack = new ItemStack(
			state.getBlock, 1, state.getMeta()
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

	def getName(state: BlockState): String = NameParser.getName(state, true, true)

	def getState(name: String): BlockState = NameParser.getState(name)

}
