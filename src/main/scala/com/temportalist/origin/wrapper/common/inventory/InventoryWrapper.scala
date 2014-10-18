package com.temportalist.origin.wrapper.common.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}

/**
 *
 *
 * @author CountryGamer
 */
class InventoryWrapper(var name: String, var inventorySize: Int, var ownerStack: ItemStack)
		extends IInventory {

	// Default Constructor
	protected var inventory: Array[ItemStack] = new Array[ItemStack](this.inventorySize)

	if (this.ownerStack != null && !this.ownerStack.hasTagCompound) {
		this.ownerStack.setTagCompound(new NBTTagCompound)
	}

	// End Constructor

	// Other Constructors
	def this(title: String, inventorySize: Int) {
		this(title, inventorySize, null)

	}

	// End Constructors

	def isItemInventory: Boolean = {
		this.ownerStack != null
	}

	def getSizeInventory: Int = {
		inventory.length
	}

	def getStackInSlot(slot: Int): ItemStack = {
		inventory(slot)
	}

	def decrStackSize(slot: Int, amount: Int): ItemStack = {
		var stack: ItemStack = getStackInSlot(slot)
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount)
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null)
				}
			}
			else {
				setInventorySlotContents(slot, null)
			}
			this.markDirty
		}
		stack
	}

	def getStackInSlotOnClosing(slot: Int): ItemStack = {
		val stack: ItemStack = getStackInSlot(slot)
		if (stack != null) {
			setInventorySlotContents(slot, null)
		}
		stack
	}

	def setInventorySlotContents(slot: Int, itemstack: ItemStack) {
		this.inventory(slot) = itemstack
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit) {
			itemstack.stackSize = this.getInventoryStackLimit
		}
		this.markDirty
	}

	def getInventoryName: String = {
		this.name
	}

	def hasCustomInventoryName: Boolean = {
		name.length > 0
	}

	def getInventoryStackLimit: Int = {
		64
	}

	def markDirty {
		{
			var i: Int = 0
			while (i < this.getSizeInventory) {
				{
					if (this.getStackInSlot(i) != null &&
							this.getStackInSlot(i).stackSize == 0) this
							.setInventorySlotContents(i, null)
				}
				{i += 1; i}
			}
		}
	}

	def isUseableByPlayer(entityplayer: EntityPlayer): Boolean = {
		true
	}

	def openInventory {
	}

	def closeInventory {
	}

	/**
	 * This method doesn't seem to do what it claims to do, as
	 * items can still be left-clicked and placed in the inventory
	 * even when this returns false
	 */
	def isItemValidForSlot(slot: Int, itemstack: ItemStack): Boolean = {
		true
	}

	/**
	 * A custom method to read our inventory from an ItemStack's NBT compound
	 */
	def readFromNBT(compound: NBTTagCompound) {
		val items: NBTTagList = compound.getTagList("ItemInventory", 10)
		var i: Int = 0
		for (i <- 0 until items.tagCount()) {
			val item: NBTTagCompound = items.getCompoundTagAt(i)
			val slot: Byte = item.getByte("Slot")
			if (slot >= 0 && slot < getSizeInventory) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item))
			}
		}
	}

	/**
	 * A custom method to write our inventory to an ItemStack's NBT compound
	 */
	def writeToNBT(tagcompound: NBTTagCompound) {
		val nbttaglist: NBTTagList = new NBTTagList
		var i: Int = 0
		for (i <- 0 until this.getSizeInventory) {
			if (this.getStackInSlot(i) != null) {
				val nbttagcompound1: NBTTagCompound = new NBTTagCompound
				nbttagcompound1.setInteger("Slot", i)
				this.getStackInSlot(i).writeToNBT(nbttagcompound1)
				nbttaglist.appendTag(nbttagcompound1)
			}
		}
		tagcompound.setTag("ItemInventory", nbttaglist)
	}

}
