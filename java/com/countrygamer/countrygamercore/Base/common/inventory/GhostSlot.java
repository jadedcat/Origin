package com.countrygamer.countrygamercore.Base.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot which holds a ghost version of whichever item is put into it
 * 
 * @author Country_Gamer
 * 
 */
public class GhostSlot extends Slot {
	
	int maxStackSize = -1;
	
	public GhostSlot(IInventory inv, int slotID, int x, int y) {
		super(inv, slotID, x, y);
	}
	
	public GhostSlot(IInventory inv, int slotID, int x, int y, int maxSize) {
		this(inv, slotID, x, y);
		this.maxStackSize = maxSize;
	}
	
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}
	
	public ItemStack ghostSlotClick(int mouseButton, EntityPlayer player) {
		if (mouseButton == 0 || mouseButton == 1) {
			InventoryPlayer invPlayer = player.inventory;
			
			ItemStack stackInSlot = this.getStack();
			ItemStack heldStack = invPlayer.getItemStack();
			
			if (stackInSlot == null) {
				if (heldStack != null && this.isItemValid(heldStack)) {
					int stackSize = mouseButton == 0 ? heldStack.stackSize : 1;
					
					if (stackSize > this.getSlotStackLimit()) {
						stackSize = this.getSlotStackLimit();
					}
					
					ItemStack ghostStack = heldStack.copy();
					ghostStack.stackSize = stackSize;
					
					this.putStack(ghostStack);
				}
			}
			else {
				this.putStack(null);
			}
			
		}
		
		return this.getStack();
	}
	
	@Override
	public int getSlotStackLimit() {
		return this.maxStackSize > 0 ? this.maxStackSize : this.inventory.getInventoryStackLimit();
	}
	
}
