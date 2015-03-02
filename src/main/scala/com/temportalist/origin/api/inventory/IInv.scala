package com.temportalist.origin.api.inventory

import com.temportalist.origin.library.common.utility.MathFuncs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util._

/**
 *
 *
 * @author TheTemportalist
 */
trait IInv extends IInventory with ISidedInventory {

	protected var slots: Array[ItemStack] = null
	protected var stackSize: Int = 64

	protected def setSlots(size: Int, maxStackSize: Int): Unit = {
		this.slots = new Array[ItemStack](size)
		this.stackSize = maxStackSize
	}

	protected def setSlots(size: Int): Unit = {
		this.setSlots(size, 64)
	}

	override def clear(): Unit = {
		for (i <- 0 until this.getSizeInventory) this.slots(i) = null
	}

	override def getInventoryStackLimit: Int = this.stackSize

	override def getSizeInventory: Int = this.slots.length

	def hasInventory(): Boolean = this.slots != null

	def isValidSlot(index: Int): Boolean =
		this.hasInventory() && MathFuncs.between(0, index, this.getSizeInventory)

	override def getStackInSlot(index: Int): ItemStack = {
		if (this.isValidSlot(index)) {
			this.slots(index)
		}
		else null
	}

	override def getStackInSlotOnClosing(index: Int): ItemStack = this.getStackInSlot(index)

	override def setInventorySlotContents(index: Int, stack: ItemStack): Unit = {
		if (this.isValidSlot(index)) {
			if (stack == null) this.slots(index) = null
			else this.slots(index) = stack.copy()
		}
	}

	override def decrStackSize(slotID: Int, decrement: Int): ItemStack = {
		if (this.isValidSlot(slotID)) {
			// check to see of stack in slot is occupied
			if (this.slots(slotID) != null) {
				// get a copy of stack in slot
				var itemStack: ItemStack = this.slots(slotID).copy()
				// if extracting more than exists
				if (itemStack.stackSize <= decrement) {
					// remove internal stack
					this.slots(slotID) = null
					// mark for resync
					this.markDirty()
					// return the extracted stack
					return itemStack
				}
				else {
					// split the stack in slot by the decrement,
					// leaving (stack in slot's stack size - decrement)
					// of the internal stack behind
					itemStack = this.slots(slotID).splitStack(decrement)

					// if the stack in slot is non existant
					if (this.slots(slotID).stackSize <= 0) {
						// remove the dead stack
						this.slots(slotID) = null
					}

					// mark for resync
					this.markDirty()
					// return the extracted stack
					return itemStack
				}
			}
		}
		// return nothing, since nothing was taken
		null
	}

	override def openInventory(playerIn: EntityPlayer): Unit = {}

	override def closeInventory(playerIn: EntityPlayer): Unit = {}

	override def isUseableByPlayer(playerIn: EntityPlayer): Boolean = true

	override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = this.isValidSlot(index)

	override def getField(id: Int): Int = 0

	override def setField(id: Int, value: Int): Unit = {}

	override def getFieldCount: Int = 0

	override def hasCustomName: Boolean = false

	override def getDisplayName: IChatComponent =
		(if (this.hasCustomName)
			new ChatComponentText(this.getName)
		else
			new ChatComponentTranslation(this.getName, new Array[AnyRef](0))
				).asInstanceOf[IChatComponent]

	override def getSlotsForFace(side: EnumFacing): Array[Int] =
		if (this.hasInventory()) {
			val slotsFromSide: Array[Int] = new Array[Int](this.getSizeInventory)
			for (i <- 0 to this.getSizeInventory) {
				slotsFromSide(i) = i
			}
			slotsFromSide
		}
		else
			null

	override def canInsertItem(
			index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean =
		this.isValidSlot(index)

	override def canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean =
		this.isValidSlot(index)

	def toNBT_IInv(tagCom: NBTTagCompound): Unit = {
		tagCom.setBoolean("has", this.hasInventory())
		if (this.hasInventory()) {
			tagCom.setInteger("size", this.getSizeInventory)

			val tagList: NBTTagList = new NBTTagList()
			for (slotID <- 0 to this.getSizeInventory) {
				if (this.getStackInSlot(slotID) != null) {
					val stackTagCom: NBTTagCompound = new NBTTagCompound()
					stackTagCom.setInteger("slot", slotID.asInstanceOf[Byte])
					this.getStackInSlot(slotID).writeToNBT(stackTagCom)
					tagList.appendTag(stackTagCom)
				}
			}
			tagCom.setTag("stacks", tagList)

			tagCom.setInteger("maxstacksize", this.stackSize)
		}
	}

	def fromNBT_IInv(tagCom: NBTTagCompound): Unit = {
		if (tagCom.getBoolean("has")) {
			this.slots = new Array[ItemStack](tagCom.getInteger("size"))

			val tagList: NBTTagList = tagCom.getTagList("stacks", 10)
			for (tagIndex <- 0 to tagList.tagCount()) {
				val stackTagCom: NBTTagCompound = tagList.getCompoundTagAt(tagIndex)
				val slotID: Int = stackTagCom.getInteger("slot") & 255
				if (slotID >= 0 && slotID < this.getSizeInventory) {
					this.slots(slotID) = ItemStack.loadItemStackFromNBT(stackTagCom)
				}
			}

			this.stackSize = tagCom.getInteger("maxstacksize")
		}
		else {
			this.slots = new Array[ItemStack](0)
			this.stackSize = 64
		}
	}

}
