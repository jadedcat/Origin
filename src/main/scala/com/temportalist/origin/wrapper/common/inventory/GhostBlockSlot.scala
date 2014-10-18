package com.temportalist.origin.wrapper.common.inventory

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author CountryGamer
 */
class GhostBlockSlot(inv: IInventory, slotID: Int, x: Int, y: Int, maxSize: Int)
		extends GhostSlot(inv, slotID, x, y, maxSize) {

	def this(inv: IInventory, slotID: Int, x: Int, y: Int) {
		this(inv, slotID, x, y, -1)

	}

	override def isItemValid(itemStack: ItemStack): Boolean = {
		val block: Block = Block.getBlockFromItem(itemStack.getItem)
		block.equals(Blocks.air) && (block.getRenderType == 0)
	}

}
