package com.countrygamer.cgo.wrapper.common.inventory

import scala.util.control.Breaks._

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, IInventory, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author CountryGamer
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

	protected def registerSlot(slotID: Int, slotX: Int, slotY: Int): Unit = {
		this.addSlotToContainer(new Slot(this.inventory, slotID, slotX, slotY))
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
		var row: Int = 0
		for (row <- 0 to 2) {
			var col: Int = 0
			for (col <- 0 to 9) {
				val slotID: Int = col + row * 9 + 9
				val x: Int = (8 + col * 18) + offsetX
				val y: Int = (84 + row * 18) + offsetY

				var setSlot: Boolean = false
				if (finalSlotIDs.length > 0) {
					var slotIndex = 0
					breakable {
						for (slotIndex <- 0 to finalSlotIDs.length) {
							if (finalSlotIDs(slotIndex) == slotID) {
								this.addSlotToContainer(
									new FinalSlot(this.player.inventory, slotID, x, y))
								setSlot = true
								break()
							}
						}
					}
				}

				if (!setSlot) {
					this.addSlotToContainer(new Slot(this.player.inventory, slotID, x, y))
				}

			}
		}

		row = 0
		var col: Int = 0
		for (col <- 0 to 9) {
			val slotID: Int = col
			val x: Int = (8 + col * 18) + offsetX
			val y: Int = 142 + offsetY

			var setSlot: Boolean = false
			breakable {
				for (slotIndex <- 0 to finalSlotIDs.length) {
					if (finalSlotIDs(slotIndex) == slotID) {
						this.addSlotToContainer(new FinalSlot(this.player.inventory, slotID, x, y))
						setSlot = true
						break()
					}
				}
			}

			if (!setSlot) {
				this.addSlotToContainer(new Slot(this.player.inventory, slotID, x, y))
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
	override def transferStackInSlot(player: EntityPlayer, slotiD: Int): ItemStack = {
		val slot: Slot = this.inventorySlots.get(slotiD).asInstanceOf[Slot]
		var stackInSlot: ItemStack = null
		if (slot != null && slot.getHasStack) {
			val stackInSlotCopy: ItemStack = slot.getStack.copy
			stackInSlot = stackInSlotCopy.copy
			val inventorySize: Int = this.getIInventory.getSizeInventory()
			val playerInventoryEnd: Int = inventorySize + 36
			val playerHotBarStartID: Int = playerInventoryEnd - 9
			if (slotiD < inventorySize) {
				if (slot.isInstanceOf[GhostSlot]) {
					return null
				}
				else if (!this.mergeItemStack(stackInSlotCopy, inventorySize, playerInventoryEnd,
					false)) {
					return null
				}
				slot.onSlotChange(stackInSlotCopy, stackInSlot)
			}
			else {
				val targetSlotID: Int = this.getSlotIDForItemStack(stackInSlotCopy)
				if (targetSlotID >= 0) {
					{
						var slotID: Int = targetSlotID
						while (slotID <
								this.getExcludedMaximumSlotIDForItemStack(stackInSlotCopy)) {
							{
								val targetSlot: Slot = this.inventorySlots.get(slotID)
										.asInstanceOf[Slot]
								if (this.isItemValidForSlotOnShift(targetSlot, stackInSlotCopy)) {
									if (!this.mergeItemStack(stackInSlotCopy, slotID, slotID + 1,
										false)) return null
								}
							}
							{slotID += 1; slotID - 1}
						}
					}
				}
				else {
					if (slotiD < playerHotBarStartID) {
						if (!this.mergeItemStack(stackInSlotCopy, playerHotBarStartID,
							playerHotBarStartID + 9, false)) {
							return null
						}
					}
					else {
						if (!this
								.mergeItemStack(stackInSlotCopy, inventorySize, playerHotBarStartID,
						            false)) {
							return null
						}
					}
				}
			}
			if (stackInSlotCopy.stackSize == 0) {
				slot.putStack(null.asInstanceOf[ItemStack])
			}
			else {
				slot.onSlotChanged
			}
			if (stackInSlotCopy.stackSize == stackInSlot.stackSize) {
				return null
			}
			slot.onPickupFromSlot(player, stackInSlotCopy)
		}
		stackInSlot
	}

	protected def getSlotIDForItemStack(stackToProcess: ItemStack): Int = {
		-1
	}

	protected def getExcludedMaximumSlotIDForItemStack(stackToProcess: ItemStack): Int = {
		this.getIInventory.getSizeInventory()
	}

	protected def isItemValidForSlotOnShift(slot: Slot, stackToProcess: ItemStack): Boolean = {
		if (slot.isInstanceOf[GhostSlot]) {
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
				this.inventorySlots.get(slotID).isInstanceOf[GhostSlot]) {
			return this.inventorySlots.get(slotID).asInstanceOf[GhostSlot]
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
