package com.countrygamer.countrygamercore.Base.common.inventory;

import com.countrygamer.countrygamercore.Base.common.item.ItemInvBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItemBase implements IInventory {
	private String name = "";
	
	/** Defining your inventory size this way is handy */
	public final int INV_SIZE;
	
	/**
	 * Inventory's size must be same as number of slots you add to the Container
	 * class
	 */
	ItemStack[] inventory;
	
	protected final Class<? extends Item> itemClass;
	
	/**
	 * @param itemstack
	 *            - the ItemStack to which this inventory belongs
	 */
	public InventoryItemBase(String title, int invSize, ItemStack itemstack,
			Class<? extends Item> itemClass) {
		this.name = title;
		this.INV_SIZE = invSize;
		this.inventory = new ItemStack[invSize];
		this.itemClass = itemClass;
		
		// Read the inventory contents from NBT
		NBTTagCompound tagCom = itemstack.getTagCompound();
		NBTTagCompound invTagCom = tagCom.getCompoundTag(ItemInvBase.inventoryDataKey);
		readFromNBT(invTagCom);
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
			else {
				setInventorySlotContents(slot, null);
			}
			
			this.markDirty();
		}
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.inventory[slot] = itemstack;
		
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
		
		this.markDirty();
	}
	
	@Override
	public String getInventoryName() {
		return name;
	}
	
	@Override
	public boolean hasCustomInventoryName() {
		return name.length() > 0;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public void markDirty() {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0)
				this.setInventorySlotContents(i, null);
		}
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void openInventory() {
	}
	
	@Override
	public void closeInventory() {
	}
	
	/**
	 * This method doesn't seem to do what it claims to do, as
	 * items can still be left-clicked and placed in the inventory
	 * even when this returns false
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return !(this.itemClass.isAssignableFrom(itemStack.getItem().getClass()));
	}
	
	/**
	 * A custom method to read our inventory from an ItemStack's NBT compound
	 */
	public void readFromNBT(NBTTagCompound compound) {
		// System.out.println("Reading");
		NBTTagList stackList = compound.getTagList("Stacks", 10);
		
		// System.out.println("list has " + stackList.tagCount() + " tags");
		
		for (int i = 0; i < stackList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = stackList.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			
			if (j >= 0 && j < this.getSizeInventory()) {
				this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
			}
		}
		
	}
	
	/**
	 * A custom method to write our inventory to an ItemStack's NBT compound
	 */
	public void writeToNBT(NBTTagCompound compound) {
		// System.out.println("Writing");
		NBTTagList stackList = new NBTTagList();
		
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (this.getStackInSlot(i) != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(nbttagcompound1);
				stackList.appendTag(nbttagcompound1);
			}
		}
		
		// System.out.println("Saved list with " + stackList.tagCount() + " tags");
		
		compound.setTag("Stacks", stackList);
		
	}
	
}
