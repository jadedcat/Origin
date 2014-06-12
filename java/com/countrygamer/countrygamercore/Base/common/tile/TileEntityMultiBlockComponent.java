package com.countrygamer.countrygamercore.Base.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityMultiBlockComponent extends TileEntityTankBase {
	
	private int[] masterCoords;
	
	public TileEntityMultiBlockComponent() {
		super("");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		boolean hasMaster = this.hasTileEntityMaster();
		tagCom.setBoolean("hasMaster", hasMaster);
		if (hasMaster) {
			tagCom.setIntArray("masterCoords", this.masterCoords);
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		if (tagCom.getBoolean("hasMaster")) {
			this.masterCoords = tagCom.getIntArray("masterCoords");
		}
		else {
			this.masterCoords = null;
		}
		
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	public void setMasterTileEntity(TileEntityMultiBlockMaster master) {
		if (master != null) {
			this.masterCoords = new int[] {
					master.xCoord, master.yCoord, master.zCoord
			};
			this.setName(master.getName());
		}
		else {
			this.masterCoords = null;
			this.setName("");
		}
	}
	
	public boolean hasTileEntityMaster() {
		return this.masterCoords != null;
	}
	
	public TileEntityMultiBlockMaster getTileEntityMaster() {
		if (this.masterCoords == null) return null;
		return (TileEntityMultiBlockMaster) this.getWorldObj().getTileEntity(this.masterCoords[0],
				this.masterCoords[1], this.masterCoords[2]);
	}
	
	public int getSizeInventory() {
		return this.hasTileEntityMaster() ? this.getTileEntityMaster().getSizeInventory() : 0;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().getStackInSlot(i);
		return null;
	}
	
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().decrStackSize(slot, amount);
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().getStackInSlotOnClosing(i);
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if (this.hasTileEntityMaster())
			this.getTileEntityMaster().setInventorySlotContents(i, itemStack);
	}
	
	@Override
	public String getInventoryName() {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().getName();
		return null;
	}
	
	@Override
	public boolean hasCustomInventoryName() {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().hasCustomInventoryName();
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().getInventoryStackLimit();
		return 0;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().isUseableByPlayer(entityplayer);
		return false;
	}
	
	@Override
	public void openInventory() {
		if (this.hasTileEntityMaster()) this.getTileEntityMaster().openInventory();
	}
	
	@Override
	public void closeInventory() {
		if (this.hasTileEntityMaster()) this.getTileEntityMaster().closeInventory();
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().isItemValidForSlot(i, itemstack);
		return false;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int i) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().getAccessibleSlotsFromSide(i);
		return null;
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int i2) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().canInsertItem(i, itemStack, i2);
		return false;
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int i2) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().canExtractItem(i, itemStack, i2);
		return false;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (this.hasTileEntityMaster()) {
			return this.getTileEntityMaster().fill(from, resource, doFill);
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().drain(from, resource, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (this.hasTileEntityMaster())
			return this.getTileEntityMaster().drain(from, maxDrain, doDrain);
		return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().canFill(from, fluid);
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (this.hasTileEntityMaster()) return this.getTileEntityMaster().canDrain(from, fluid);
		return false;
	}
	
}
