package com.countrygamer.countrygamercore.Base.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityTankBase extends TileEntityInventoryBase implements IFluidHandler {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Global Variables ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Holds the tank information
	 */
	private FluidTank tank;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public TileEntityTankBase(String name) {
		this(name, 0);
	}
	
	public TileEntityTankBase(String name, int tankSize) {
		this(name, tankSize, 0, 0);
	}
	
	public TileEntityTankBase(String name, int inventorySize, int maxStackSize, int tankSize) {
		super(name, inventorySize, maxStackSize);
		
		if (tankSize > 0) {
			this.tank = new FluidTank(tankSize);
		}
		;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~ Saving/Loading NBT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		tagCom.setBoolean("hasTank", this.tank != null);
		if (this.tank != null) {
			tagCom.setInteger("tankCapacity", this.getTankCapacity());
			
			tagCom.setTag("tankNBT", this.tank.writeToNBT(new NBTTagCompound()));
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		if (tagCom.getBoolean("hasTank")) {
			if (this.tank == null) {
				this.tank = new FluidTank(tagCom.getInteger("tankCapacity"));
			}
			
			this.tank.readFromNBT(tagCom.getCompoundTag("tankNBT"));
			
		}
		
	}
	
	public void writeTankToNBT(NBTTagCompound tagCom) {
		this.tank.writeToNBT(tagCom);
	}
	
	public boolean loadTankFromNBT(NBTTagCompound tankTag) {
		if (this.canModifyTank()) {
			this.tank.readFromNBT(tankTag);
			return true;
		}
		return false;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~ Usage Tank Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean canModifyTank() {
		return this.tank != null;
	}
	
	public FluidStack getFluidStack() {
		return this.tank != null ? this.tank.getFluid() : null;
	}
	
	public int getTankCapacity() {
		if (this.canModifyTank()) {
			return this.tank.getCapacity();
		}
		return 0;
	}
	
	public void clearTank() {
		if (this.tank != null) this.tank.setFluid(null);
	}
	
	public boolean canHoldMoreFluid() {
		if (this.tank != null) {
			if (this.tank.getFluidAmount() < this.getTankCapacity()) {
				return true;
			}
		}
		return false;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Required Tank Functions ~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!this.canModifyTank()) return 0;
		
		int fluidAmount = this.tank.fill(resource, doFill);
		
		if (fluidAmount > 0 && doFill) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		
		return fluidAmount;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (!this.canModifyTank()) return null;
		
		FluidStack fluidStack = tank.drain(maxDrain, doDrain);
		
		if (fluidStack != null && doDrain) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		
		return fluidStack;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (!this.canModifyTank()) return false;
		return this.tank.getFluidAmount() < this.tank.getCapacity();
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (!this.canModifyTank()) return false;
		return this.tank.getFluidAmount() > 0;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if (!this.canModifyTank()) return null;
		
		FluidStack containedFluid = null;
		if (this.tank.getFluid() != null) containedFluid = tank.getFluid().copy();
		return new FluidTankInfo[] {
			new FluidTankInfo(containedFluid, tank.getCapacity())
		};
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
