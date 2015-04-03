package com.temportalist.origin.wrapper.common.inventory

import com.temportalist.origin.library.common.container.{SlotGhost, SlotFinal}
import com.temportalist.origin.library.common.utility.Stacks

import scala.util.control.Breaks._

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, IInventory, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
class ContainerWrapper(var player: EntityPlayer, var inventory: IInventory) extends Container() {

	// Default Constructor
	this.registerSlots()
	var needsUpdate = false

	// End Constructor

	// Other Constructors

	// End Constructors

	// ~~~~~~~~~~~~~~~~~~~~ Register Slots ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Used to register slots for this container
	 * Subclasses SHOULD use this method (that is the reason we have containers),
	 * however, subclasses do not NEED to use this method.
	 */
	protected def registerSlots(): Unit = {
	}

	protected def registerSlot(slotID: Int, slotX: Int, slotY: Int, isFinal: Boolean): Unit = {
		if (isFinal) {
			this.addSlotToContainer(new SlotFinal(this.inventory, slotID, slotX, slotY))
		}
		else {
			this.addSlotToContainer(new Slot(this.inventory, slotID, slotX, slotY))
		}
	}

	/**
	 * Method to auto-generate slots connected to this player's inventory
	 *
	 * @param offsetX
	 * @param offsetY
	 */
	protected def registerPlayerSlots(offsetX: Int, offsetY: Int): Unit = {
		this.registerPlayerSlots(offsetX, offsetY, new Array[Int](0))
	}

	protected def registerPlayerSlots(offsetX: Int, offsetY: Int,
			finalSlotIDs: Array[Int]): Unit = {
		val slotSize: Int = 18
		val startX: Int = 12 + offsetX
		val startY: Int = 84 + offsetY
		for (col <- 0 until 9) {
			val x: Int = col * slotSize + startX
			var y: Int = startY
			this.addSlotToContainer(new Slot(this.player.inventory,
				col, x, startY + 67
			))
			for (row <- 0 until 3) {
				y = (startY + row * slotSize) + 9
				this.addSlotToContainer(new Slot(this.player.inventory,
					(row + 1) * 9 + col, x, y
				))
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~ Get Inventory !~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Get the invenotry to which this container belongs
	 *
	 * @return
	 */
	def getIInventory(): IInventory = {
		this.inventory
	}

	/**
	 * Discern whether or not the inventory that this container belongs to is a tile entity
	 *
	 * @return
	 */
	def isAttachedToTileEntity(): Boolean = {
		this.inventory.isInstanceOf[TileEntity]
	}

	/**
	 * Get the tile entity to which this container belongs
	 *
	 * @return
	 */
	def getTileEntity(): TileEntity = {
		if (this.isAttachedToTileEntity()) {
			return this.inventory.asInstanceOf[TileEntity]
		}
		null
	}

	/**
	 * Discern whether or not the inventory that this container belongs to is an Item
	 *
	 * @return
	 */
	def isAttachedToItem(): Boolean = {
		this.inventory.isInstanceOf[InventoryWrapper] &&
				this.inventory.asInstanceOf[InventoryWrapper].isItemInventory
	}

	/**
	 * Get the item inventory to which this container belongs
	 *
	 * @return
	 */
	def getItemInventory(): InventoryWrapper = {
		if (this.isAttachedToItem()) {
			return this.inventory.asInstanceOf[InventoryWrapper]
		}
		null
	}

	// ~~~~~~~~~~~~~~~~~~~~ Interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Discern whether or not the passed player can use this container.
	 */
	override def canInteractWith(player: EntityPlayer): Boolean = {
		this.inventory.isUseableByPlayer(player)
	}

	/**
	 * Used to move item stacks about when a player shiftclicks
	 */
	///*
	override def transferStackInSlot(player: EntityPlayer, slotID: Int): ItemStack = {
		val slot: Slot = this.inventorySlots.get(slotID).asInstanceOf[Slot]
		var stackInSlot: ItemStack = null
		if (slot != null && slot.getHasStack) {
			stackInSlot = slot.getStack
			val stackInSlotCopy: ItemStack = stackInSlot.copy()

			val extraSlotsStart: Int = 0
			val playerInvEnd: Int = extraSlotsStart + 27 // length of player inventory
			val playerHotBarEnd: Int = playerInvEnd + 9 // length of hotbar
			val thisInvEnd: Int = playerHotBarEnd + this.getIInventory().getSizeInventory

			if (slotID < extraSlotsStart) {
				if (!this.mergeItemStack(
					stackInSlotCopy, extraSlotsStart, playerHotBarEnd, isBackwards = true))
					return null
			}
			else if (slotID < playerHotBarEnd) {
				/*
				if (i > 0 && ...) // can insert into extra slots
					if (!this.mergeItemStack(stackInSlotCopy, 0, i, isBackwards = false))
						return null
				else
				*/
				if (!this.mergeItemStack(
					stackInSlotCopy, playerHotBarEnd, thisInvEnd, isBackwards = false)) return null
			}
			else if (!this.mergeItemStack(
				stackInSlotCopy, extraSlotsStart, playerHotBarEnd, isBackwards = true))
				return null
			if (stackInSlotCopy.stackSize <= 0) slot.putStack(null)
			else slot.onSlotChanged()
			if (stackInSlotCopy.stackSize == stackInSlot.stackSize)
				return null
		}
		stackInSlot
	}

	//*/
	/*
	override def transferStackInSlot(player: EntityPlayer, slotIndex: Int): ItemStack = {
		var local: ItemStack = null
		val slot: Slot = this.inventorySlots.get(slotIndex).asInstanceOf[Slot]
		val altSize: Int = 0
		val altMax: Int = altSize + 27
		val k: Int = altMax + 9
		val m: Int = k + 3 // k + local slot inventory size
		if (slot != null && slot.getHasStack) {
			val local2: ItemStack = slot.getStack
			local = local2.copy()
			if (slotIndex < altSize) {
				if (!mergeItemStack(local2, altSize, k, true)) return null
			}
			else if (slotIndex < k) {
				// this section checked augments:
				/*
				if ((!this.augmentLock) && (i > 0) &&
						(AugmentHelper.isAugmentItem(localItemStack2))) {
					if (!mergeItemStack(localItemStack2, 0, i, false))
						return null;
				}
				else if (!mergeItemStack(localItemStack2, k, m, false))
					return null;
				 */
				if (altSize > 0) {
					if (!mergeItemStack(local2, 0, altSize, false))
						return null
				}
				else if (!mergeItemStack(local2, k, m, false))
					return null
			}
			else if (!mergeItemStack(local2, altSize, k, true)) return null

			if (local2.stackSize <= 0) slot.putStack(null)
			else slot.onSlotChanged()

			if (local2.stackSize == local.stackSize) return null
		}
		local
	}
	*/

	override def mergeItemStack(stack: ItemStack, minSlotID: Int, maxSlotID: Int,
			isBackwards: Boolean): Boolean = {
		var retStack: Boolean = false
		var slotID: Int = if (isBackwards) maxSlotID - 1 else minSlotID
		var slot: Slot = null
		var local: ItemStack = null
		if (stack.isStackable) {
			while (stack.stackSize > 0 && (
					(!isBackwards && slotID < maxSlotID) ||
							(isBackwards && slotID >= minSlotID)
					)) {
				slot = this.inventorySlots.get(slotID).asInstanceOf[Slot]
				local = slot.getStack
				if (slot.isItemValid(stack) && Stacks.doStacksMatch(stack, local,
					meta = true, size = false, nbt = true, nil = false
				)) {
					val k: Int = local.stackSize + stack.stackSize
					val m: Int = Math.min(stack.getMaxStackSize, slot.getSlotStackLimit)
					if (k <= m) {
						stack.stackSize = 0
						local.stackSize = k
						slot.onSlotChanged()
						retStack = true
					}
					else if (local.stackSize < m) {
						stack.stackSize -= m - local.stackSize
						local.stackSize = m
						slot.onSlotChanged()
						retStack = true
					}
				}
				slotID += (if (isBackwards) -1 else 1)
			}
		}
		if (stack.stackSize > 0) {
			slotID = if (isBackwards) maxSlotID - 1 else minSlotID

			breakable {
				while ((!isBackwards && slotID < maxSlotID) ||
						(isBackwards && slotID >= minSlotID)) {
					slot = this.inventorySlots.get(slotID).asInstanceOf[Slot]
					local = slot.getStack
					if (slot.isItemValid(stack) && local == null) {
						val nextStack: ItemStack = stack.copy()
						nextStack.stackSize = Math.min(stack.stackSize, slot.getSlotStackLimit)
						slot.putStack(nextStack)
						slot.onSlotChanged()
						if (slot.getStack != null) {
							stack.stackSize -= slot.getStack.stackSize
							retStack = true
						}
						break
					}
					slotID += (if (isBackwards) -1 else 1)
				}
			}
		}

		retStack
	}

	protected def getSlotIDForItemStack(stackToProcess: ItemStack): Int = {
		-1
	}

	protected def getExcludedMaximumSlotIDForItemStack(stackToProcess: ItemStack): Int = {
		this.getIInventory.getSizeInventory()
	}

	protected def isItemValidForSlotOnShift(slot: Slot, stackToProcess: ItemStack): Boolean = {
		if (slot.isInstanceOf[SlotGhost]) {
			false
		}
		else {
			slot.isItemValid(stackToProcess)
		}
	}

	override def slotClick(slotID: Int, mouseButton: Int, flag: Int,
			player: EntityPlayer): ItemStack = {
		if (this.isAttachedToItem()) {
			this.needsUpdate = true
		}
		if (slotID >= 0 && slotID < this.inventorySlots.size() &&
				this.inventorySlots.get(slotID).isInstanceOf[SlotGhost]) {
			return this.inventorySlots.get(slotID).asInstanceOf[SlotGhost]
					.ghostSlotClick(mouseButton, player)
		}
		super.slotClick(slotID, mouseButton, flag, player)
	}

	// ~~~~~~~~~~~~~~~~~~~~ Item NBT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	def writeToNBT(): Unit = {
		if (this.isAttachedToItem()) {
			val itemStack: ItemStack = this.player.getHeldItem
			if (itemStack != null) {
				val tagCom: NBTTagCompound = itemStack.getTagCompound
				val inventoryTagCom: NBTTagCompound = new NBTTagCompound()
				this.inventory.asInstanceOf[InventoryWrapper].writeToNBT(inventoryTagCom)
				tagCom.setTag("InventoryData", inventoryTagCom)
				itemStack.setTagCompound(tagCom)
			}
		}
	}

}
