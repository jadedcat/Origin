package com.countrygamer.cgo.wrapper.common.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 * A default fake inventory for easy access
 *
 * @param holderStack
 * The itemStack used to instantiate this inventory
 * @param inventorySize
 * The size of this inventory (number of slots)
 * @param stackLimit
 * The maximum stack size per slot
 *
 * @author CountryGamer
 */
class FakeInventory(var holderStack: ItemStack, var inventorySize: Int, var stackLimit: Int)
		extends IInventory {

	// Default Constructor
	private val inv: Array[ItemStack] = new Array[ItemStack](this.inventorySize)
	// End Constructor

	def getSizeInventory: Int = {
		return this.inventorySize
	}

	def getStackInSlot(slot: Int): ItemStack = {
		return this.inv(slot)
	}

	def decrStackSize(slot: Int, amount: Int): ItemStack = {
		if (this.inv(slot) != null) {
			var itemstack: ItemStack = null
			if (this.inv(slot).stackSize <= amount) {
				itemstack = this.inv(slot)
				this.inv(slot) = null
				this.markDirty
				return itemstack
			}
			else {
				itemstack = this.inv(slot).splitStack(amount)
				if (this.inv(slot).stackSize == 0) {
					this.inv(slot) = null
				}
				this.markDirty
				return itemstack
			}
		}
		else {
			return null
		}
	}

	def getStackInSlotOnClosing(slot: Int): ItemStack = {
		return this.getStackInSlot(slot)
	}

	def setInventorySlotContents(slot: Int, stack: ItemStack) {
		this.inv(slot) = stack
		this.markDirty
	}

	def getInventoryName: String = {
		return if (this.holderStack != null) this.holderStack.getDisplayName else ""
	}

	def hasCustomInventoryName: Boolean = {
		return false
	}

	def getInventoryStackLimit: Int = {
		return this.stackLimit
	}

	def markDirty {
	}

	def isUseableByPlayer(var1: EntityPlayer): Boolean = {
		return true
	}

	def openInventory {
	}

	def closeInventory {
	}

	def isItemValidForSlot(slot: Int, stack: ItemStack): Boolean = {
		return true
	}

}

