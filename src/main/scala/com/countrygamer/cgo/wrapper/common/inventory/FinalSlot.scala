package com.countrygamer.cgo.wrapper.common.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 * A slot you cannot interfere with
 *
 * @author CountryGamer
 */
class FinalSlot(inventory: IInventory, slotID: Int, x: Int, y: Int)
		extends OutputSlot(inventory, slotID, x, y) {

	override def isItemValid(itemStack: ItemStack): Boolean = {
		return false
	}

	override def canTakeStack(player: EntityPlayer): Boolean = {
		return false
	}

}
