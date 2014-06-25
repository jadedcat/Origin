package com.countrygamer.countrygamercore.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class FakeInventory implements IInventory {
	
	private ItemStack holderStack;
	private ItemStack[] inv;
	private final int invSize;
	private final int stackLimit;
	
	public FakeInventory(ItemStack itemStack, int inventorySize, int maxStackSizePerSlot) {
		this.holderStack = itemStack;
		this.invSize = inventorySize;
		this.inv = new ItemStack[this.invSize];
		this.stackLimit = maxStackSizePerSlot;
		
	}
	
	@Override
	public int getSizeInventory() {
		return this.invSize;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inv[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.inv[slot] != null) {
			ItemStack itemstack;
			
			if (this.inv[slot].stackSize <= amount) {
				itemstack = this.inv[slot];
				this.inv[slot] = null;
				this.markDirty();
				return itemstack;
			}
			else {
				itemstack = this.inv[slot].splitStack(amount);
				
				if (this.inv[slot].stackSize == 0) {
					this.inv[slot] = null;
				}
				
				this.markDirty();
				return itemstack;
			}
		}
		else {
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return this.getStackInSlot(slot);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv[slot] = stack;
		this.markDirty();
	}
	
	@Override
	public String getInventoryName() {
		return this.holderStack != null ? this.holderStack.getDisplayName() : "";
	}
	
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return this.stackLimit;
	}
	
	@Override
	public void markDirty() {
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}
	
	@Override
	public void openInventory() {
		
	}
	
	@Override
	public void closeInventory() {
		
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
}
