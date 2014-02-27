package com.countrygamer.countrygamer_core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.countrygamer_core.block.tiles.TileEntityInventoryBase;

public class ContainerBlockBase extends Container {
	
	public TileEntityInventoryBase	tileEnt;
	
	public ContainerBlockBase(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		this.tileEnt = tileEnt;
		this.registerSlots(invPlayer);
	}
	
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
	}
	
	protected void registerPlayerSlots(InventoryPlayer inventoryPlayer, int offsetX, int offsetY) {
		int i;
		// PLAYER INVENTORY - uses default locations for standard inventory
		// texture file
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, (8 + j * 18)
						+ offsetX, (84 + i * 18) + offsetY));
			}
		}
		
		// PLAYER ACTION BAR - uses default locations for standard action bar
		// texture file
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, (8 + i * 18) + offsetX,
					142 + offsetY));
		}
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEnt.isUseableByPlayer(player);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slotiD) {
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotiD);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemStack = itemstack1.copy();
			
			if (slotiD < this.tileEnt.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, this.tileEnt.getSizeInventory(),
						inventorySlots.size(), true)) return null;
				slot.onSlotChange(itemstack1, itemStack);
			}
			else {
				if (!this.shiftClick(itemstack1, slotiD)) {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemStack.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemStack;
	}
	
	protected boolean shiftClick(ItemStack itemStack, int slotiD) {
		return false;
	}
	
}
