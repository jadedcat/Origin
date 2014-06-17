package com.countrygamer.countrygamercore.Base.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.countrygamercore.Base.common.item.ItemInvBase;
import com.countrygamer.countrygamercore.Base.common.tile.TileEntityInventoryBase;

/**
 * Base class for all Containers
 * 
 * @author Country_Gamer
 * 
 */
public class ContainerBase extends Container {
	
	/**
	 * The player which opened this container
	 */
	public final EntityPlayer player;
	
	/**
	 * The inventory to which this container belongs
	 */
	private final IInventory inventory;
	
	/**
	 * Used by items to save NBT data
	 */
	public boolean needsUpdate;
	
	public ContainerBase(EntityPlayer player, IInventory inventory) {
		this.player = player;
		this.inventory = inventory;
		
		this.registerSlots();
	}
	
	/**
	 * Get the inventory to which this container belongs
	 * 
	 * @return
	 */
	public IInventory getIInventory() {
		return this.inventory;
	}
	
	/**
	 * Discern whether or not the inventory that this container belongs to is a tile entity
	 * 
	 * @return
	 */
	public boolean isAttachedToTileEntity() {
		return this.inventory instanceof TileEntityInventoryBase;
	}
	
	/**
	 * Get the tile entity to which this container belongs
	 * 
	 * @return
	 */
	public TileEntityInventoryBase getTileEntity() {
		if (this.isAttachedToTileEntity()) return (TileEntityInventoryBase) this.inventory;
		return null;
	}
	
	/**
	 * Discern whether or not the inventory that this container belongs to is an Item
	 * 
	 * @return
	 */
	public boolean isAttachedToItem() {
		return this.inventory instanceof InventoryItemBase;
	}
	
	/**
	 * Get the item inventory to which this container belongs
	 * 
	 * @return
	 */
	public InventoryItemBase getItemInventory() {
		if (this.isAttachedToItem()) return (InventoryItemBase) this.inventory;
		return null;
	}
	
	/**
	 * Used to register slots for this container
	 * Subclasses SHOULD use this method (that is the reason we have containers),
	 * however, subclasses do not NEED to use this method.
	 */
	protected void registerSlots() {
	}
	
	protected void registerSlot(int slotID, int slotX, int slotY) {
		this.addSlotToContainer(new Slot(this.inventory, slotID, slotX, slotY));
	}
	
	/**
	 * Method to auto-generate slots connected to this player's inventory
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
	protected void registerPlayerSlots(int offsetX, int offsetY) {
		this.registerPlayerSlots(offsetX, offsetY, new int[] {});
	}
	
	protected void registerPlayerSlots(int offsetX, int offsetY, int[] finalSlotIDs) {
		int i;
		// PLAYER INVENTORY - uses default locations for standard inventory
		// texture file
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int id = j + i * 9 + 9;
				int x = (8 + j * 18) + offsetX;
				int y = (84 + i * 18) + offsetY;
				
				boolean setSlot = false;
				for (int slotIndex = 0; slotIndex < finalSlotIDs.length; slotIndex++) {
					if (finalSlotIDs[slotIndex] == id) {
						this.addSlotToContainer(new SlotFinal(this.player.inventory, id, x, y));
						setSlot = true;
						break;
					}
				}
				if (!setSlot) this.addSlotToContainer(new Slot(this.player.inventory, id, x, y));
			}
		}
		
		// PLAYER ACTION BAR - uses default locations for standard action bar
		// texture file
		for (i = 0; i < 9; ++i) {
			int id = i;
			int x = (8 + i * 18) + offsetX;
			int y = 142 + offsetY;
			
			boolean setSlot = false;
			for (int slotIndex = 0; slotIndex < finalSlotIDs.length; slotIndex++) {
				if (finalSlotIDs[slotIndex] == id) {
					this.addSlotToContainer(new SlotFinal(this.player.inventory, id, x, y));
					setSlot = true;
					break;
				}
			}
			if (!setSlot) this.addSlotToContainer(new Slot(this.player.inventory, id, x, y));
			
		}
	}
	
	/**
	 * Discern whether or not the passed player can use this container.
	 */
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		if (this.isAttachedToTileEntity())
			return ((TileEntityInventoryBase) this.inventory).isUseableByPlayer(this.player);
		else if (this.isAttachedToItem()) return true;
		return false;
	}
	
	/**
	 * Used to move item stacks about when a player shiftclicks
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotiD) {
		Slot slot = (Slot) this.inventorySlots.get(slotiD);
		ItemStack stackInSlot = null;
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlotCopy = slot.getStack().copy();
			stackInSlot = stackInSlotCopy.copy();
			
			int inventorySize = this.getIInventory().getSizeInventory();
			int playerInventoryEnd = inventorySize + 36;
			int playerHotBarStartID = playerInventoryEnd - 9;
			
			if (slotiD < inventorySize) {
				// slot in this container's inventory
				
				if (slot instanceof GhostSlot) {
					return null;
				}
				else if (!this.mergeItemStack(stackInSlotCopy, inventorySize, playerInventoryEnd,
						false)) {
					return null;
				}
				
				slot.onSlotChange(stackInSlotCopy, stackInSlot);
			}
			else {
				// slot in player's inventory
				/*
				// try to put in this inventory
				if (!this.mergeItemStack(stackInSlotCopy, 0, inventorySize , true)) {
					return null;
				}
				// try to put in hotbar
				else*/
				
				int targetSlotID = this.getSlotIDForItemStack(stackInSlotCopy);
				if (targetSlotID >= 0) {
					Slot targetSlot = (Slot) this.inventorySlots.get(targetSlotID);
					if (this.isItemValidForSlot(targetSlot, stackInSlotCopy)) {
						if (!this.mergeItemStack(stackInSlotCopy, targetSlotID,
								this.getExcludedMaximumSlotIDForItemStack(stackInSlotCopy), false))
							return null;
					}
				}
				else {
					if (slotiD < playerHotBarStartID) {
						// slot not part of hotbar
						if (!this.mergeItemStack(stackInSlotCopy, playerHotBarStartID,
								playerHotBarStartID + 9, false)) {
							return null;
						}
					}
					// try to put in player inventory
					else {
						// slot is in hotbar
						if (!this.mergeItemStack(stackInSlotCopy, inventorySize,
								playerHotBarStartID, false)) {
							return null;
						}
					}
				}
				
			}
			
			if (stackInSlotCopy.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (stackInSlotCopy.stackSize == stackInSlot.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(player, stackInSlotCopy);
		}
		
		return stackInSlot;
	}
	
	protected int getSlotIDForItemStack(ItemStack stackToProcess) {
		return -1;
	}
	
	protected int getExcludedMaximumSlotIDForItemStack(ItemStack stackToProcess) {
		return this.getIInventory().getSizeInventory();
	}
	
	protected boolean isItemValidForSlot(Slot slot, ItemStack stackToProcess) {
		return slot.isItemValid(stackToProcess);
	}
	
	@Override
	public ItemStack slotClick(int slotID, int buttonPressed, int flag, EntityPlayer player) {
		if (this.isAttachedToItem()) {
			this.needsUpdate = true;
			/*
			System.out.println();
			if (slotID == this.thisItemIndex) {
			System.out.println("Same index");
			Slot slot = (Slot) this.inventorySlots.get(slotID);
			if (slot != null
					&& Container.func_94527_a(slot, player.inventory.getItemStack(), true)) {
				System.out.println("Opener item slot");
			}
			}
			 */
		}
		if (slotID >= 0 && slotID < this.inventorySlots.size()
				&& this.inventorySlots.get(slotID) instanceof GhostSlot) {
			return ((GhostSlot) this.inventorySlots.get(slotID)).ghostSlotClick(buttonPressed,
					player);
		}
		return super.slotClick(slotID, buttonPressed, flag, player);
	}
	
	public void writeToNBT() {
		if (this.isAttachedToItem()) {
			ItemStack heldStack = this.player.getHeldItem();
			if (heldStack != null) {
				NBTTagCompound tagCom = heldStack.getTagCompound();
				NBTTagCompound invTagCom = new NBTTagCompound();
				((InventoryItemBase) this.inventory).writeToNBT(invTagCom);
				tagCom.setTag(ItemInvBase.inventoryDataKey, invTagCom);
				
				this.player.getHeldItem().setTagCompound(tagCom);
			}
		}
	}
	
}
