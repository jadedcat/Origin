package com.countrygamer.countrygamercore.base.common.tile;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityInventoryBase extends TileEntityBase implements IInventory, ISidedInventory {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~ Global Variables ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * The data for storing this inventory
	 */
	private ItemStack[] inv;
	/**
	 * The manimum size of a stack per slot
	 */
	private int maxStackSize;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~ Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public TileEntityInventoryBase(String name) {
		this(name, 0, 0);
	}
	
	public TileEntityInventoryBase(String name, int inventorySize, int maxStackSizePerSlot) {
		super(name);
		
		this.inv = new ItemStack[inventorySize];
		this.maxStackSize = maxStackSizePerSlot;
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~ Saving and Loading inventory ~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		this.writeInventoryToNBT(tagCom);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		this.readInventoryFromNBT(tagCom);
		
	}
	
	public void writeInventoryToNBT(NBTTagCompound tagCom) {
		tagCom.setInteger("sizeInv", this.inv.length);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < this.inv.length; ++i) {
			if (this.inv[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inv[i].writeToNBT(nbttagcompound1);
				tagList.appendTag(nbttagcompound1);
			}
		}
		tagCom.setTag("Items", tagList);
		
		tagCom.setInteger("maxStackSize", this.maxStackSize);
		
	}
	
	public void readInventoryFromNBT(NBTTagCompound tagCom) {
		NBTTagList tagList = tagCom.getTagList("Items", 10);
		this.inv = new ItemStack[tagCom.getInteger("sizeInv")];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = tagList.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			
			if (j >= 0 && j < this.inv.length) {
				this.inv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		
		this.maxStackSize = tagCom.getInteger("maxStackSize");
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~ Dropping the Inventory ~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean shouldDropInventory() {
		return true;
	}
	
	@Override
	public void getTileEntityDrops(ArrayList<ItemStack> drops) {
		super.getTileEntityDrops(drops);
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.inv[i];
			if (stack != null) {
				drops.add(stack.copy());
			}
		}
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Inventory methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public int getSizeInventory() {
		return this.inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		if (this.inv != null && i < this.inv.length) {
			return this.inv[i];
		}
		return null;
	}
	
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.inv[slot] == null) {
			return null;
		}
		if (this.inv[slot].stackSize <= amount) {
			amount = this.inv[slot].stackSize;
		}
		ItemStack stack = this.inv[slot].splitStack(amount);
		
		if (this.inv[slot].stackSize <= 0) {
			this.inv[slot] = null;
		}
		this.markDirty();
		return stack;
	}
	
	public boolean addToStack(int slotID, ItemStack itemStack) {
		if (this.checkSlotAvailibility(slotID, itemStack)) {
			ItemStack newItemStack = this.getStackInSlot(slotID).copy();
			newItemStack.stackSize += itemStack.stackSize;
			this.setInventorySlotContents(slotID, newItemStack);
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return this.getStackInSlot(i);
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		this.inv[i] = itemStack == null ? null : itemStack.copy();
		this.markDirty();
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord,
				this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 10);
		this.getWorldObj().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public String getInventoryName() {
		return this.getName();
	}
	
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return this.maxStackSize;
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
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] ret = new int[this.getSizeInventory()];
		for (int i = 0; i < this.getSizeInventory(); i++)
			ret[i] = i;
		return ret;
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int side) {
		return this.checkSlotAvailibility(i, itemStack);
	}
	
	public boolean checkSlotAvailibility(int slotID, ItemStack itemStack) {
		if (this.getStackInSlot(slotID) != null) {
			ItemStack stackInSlot = this.getStackInSlot(slotID).copy();
			
			boolean sameItem = stackInSlot.getItem() == itemStack.getItem();
			boolean sameMeta = stackInSlot.getItemDamage() == itemStack.getItemDamage();
			boolean sameNBT = ItemStack.areItemStackTagsEqual(stackInSlot, itemStack);
			
			if (sameItem && sameMeta && sameNBT) {
				if (stackInSlot.stackSize + itemStack.stackSize <= stackInSlot.getMaxStackSize()) {
					return true;
				}
			}
			
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int side) {
		return true;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
