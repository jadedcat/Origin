package com.countrygamer.countrygamer_core.Blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityInventoryBase extends TileEntity implements IInventory {

	protected ItemStack[] inv;
	protected String name;
	protected int maxStackSize;

	public TileEntityInventoryBase(String name, int inventorySize,
			int maxStackSize) {
		this.name = name;
		this.inv = new ItemStack[inventorySize];
		this.maxStackSize = maxStackSize;
	}

	@Override
	public int getSizeInventory() {
		return this.inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.inv[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if ((i >= 0) && (i < this.inv.length)) {
			ItemStack itemStack = this.inv[i];
			this.inv[i] = null;
			this.markDirty();
			return itemStack;
		}
		return null;
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
			// System.out.println("Loaded stack at slot " + b0);
		}

	}

	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		//System.out.println("Writing");
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

}
