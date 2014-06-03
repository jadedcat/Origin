package com.countrygamer.countrygamercore.Base.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot which holds a ghost version of whichever item is put into it
 * @author Country_Gamer
 *
 */
public class GhostSlot extends Slot {

	public GhostSlot(IInventory inv, int slotID, int x, int y) {
		super(inv, slotID, x, y);
	}
	
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}
	
	public ItemStack ghostSlotClick(GhostSlot slot, int mouseButton, int modifier, EntityPlayer player) {
		ItemStack stack = null;
		
		if (mouseButton == 0 || mouseButton == 1){
			InventoryPlayer invPlayer = player.inventory;
			slot.onSlotChanged();
			ItemStack stackInSlot = slot.getStack();
			ItemStack heldStack = invPlayer.getItemStack();
			
			if (stackInSlot != null) {
				stack = stackInSlot.copy();
			}
			
			if (stackInSlot != null)  {
				slot.putStack(null);
			}
			
			if (stackInSlot == null) {
				if (heldStack != null && slot.isItemValid(heldStack)) {
					int stackSize = mouseButton == 0 ? heldStack.stackSize : 1;
					if (stackSize > slot.getSlotStackLimit()) {
						stackSize = slot.getSlotStackLimit();
					}
					
					ItemStack ghostStack = heldStack.copy();
					ghostStack.stackSize = stackSize;
					
					slot.putStack(ghostStack);
				}
			}
		}
		else {
			
		}
		return stack;
	}
	
}
