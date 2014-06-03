package com.countrygamer.countrygamercore.Base.common.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.countrygamer.countrygamercore.lib.UtilDrops;

public class TileEntityInventoryBase extends TileEntityBase implements IInventory, ISidedInventory {
	
	protected ItemStack[] inv;
	protected String name;
	protected int maxStackSize;
	
	public TileEntityInventoryBase(String name, int inventorySize, int maxStackSize) {
		this(name, inventorySize, maxStackSize, -1);
	}
	
	public TileEntityInventoryBase(String name, int inventorySize, int maxStackSize, int tankSize) {
		super(tankSize);
		this.name = name;
		this.inv = new ItemStack[inventorySize];
		this.maxStackSize = maxStackSize;
	}
	
	@Override
	public void invalidate() {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		this.getTileEntityDrops(drops);
		UtilDrops.spawnDrops(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, drops);
		super.invalidate();
	}
	
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
	public ItemStack getStackInSlotOnClosing(int i) {
		return this.getStackInSlot(i);
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		this.inv[i] = itemStack;
		this.markDirty();
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord,
				this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 10);
	}
	
	@Override
	public String getInventoryName() {
		return this.name;
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
		return false;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int i) {
		return null;
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int i2) {
		return false;
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int i2) {
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		NBTTagList tagList = tagCom.getTagList("Items", 10);
		this.inv = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = tagList.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			
			if (j >= 0 && j < this.inv.length) {
				this.inv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
			// System.out.println("Loaded stack at slot " + j);
		}
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		// System.out.println("Writing");
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
		
	}
	
	public boolean shouldDropInventory() {
		return true;
	}
	
}
