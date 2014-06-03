package com.countrygamer.countrygamercore.Base.common.tileentity;

import java.util.ArrayList;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.countrygamer.countrygamercore.Base.common.block.IRedstoneState;
import com.countrygamer.countrygamercore.lib.RedstoneState;

public class TileEntityBase extends TileEntity implements IRedstoneState, IFluidHandler {
	
	private Class<? extends TileEntitySpecialRenderer> specialRendererClass = null;
	private boolean hasPower = false;
	private RedstoneState redstoneState;
	
	private FluidTank tank;
	
	public TileEntityBase() {
		this(-1);
	}
	
	public TileEntityBase(int tankSize) {
		this.setRedstoneState(RedstoneState.HIGH);
		if (tankSize > 0) {
			this.tank = new FluidTank(tankSize);
		}
	}
	
	@Override
	public void updateEntity() {
	}
	
	// Client Server Sync
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCom = new NBTTagCompound();
		this.writeToNBT(tagCom);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
				this.blockMetadata, tagCom);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setBoolean("base_hasPower", this.hasPower);
		tagCom.setInteger("redstoneStateID", RedstoneState.getIntFromState(this.redstoneState));
		
		tagCom.setBoolean("hasTank", this.tank != null);
		if (this.tank != null) {
			tagCom.setInteger("tankCapacity", this.getTankCapacity());
			
			FluidStack storedFluid = this.getFluidStack();
			tagCom.setBoolean("hasFluid", storedFluid != null);
			if (storedFluid != null) {
				tagCom.setInteger("fluidID", storedFluid.fluidID);
				tagCom.setInteger("fluidAmount", storedFluid.amount);
			}
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.hasPower = tagCom.getBoolean("base_hasPower");
		this.redstoneState = RedstoneState.getStateFromInt(tagCom.getInteger("redstoneStateID"));
		
		if (tagCom.getBoolean("hasTank")) {
			// System.out.println("Should have tank");
			// System.out.println("Has tank: " + (this.tank != null));
			
			if (this.tank == null) {
				this.tank = new FluidTank(tagCom.getInteger("tankCapacity"));
			}
			
			// System.out.println("Has tank: " + (this.tank != null));
			
			if (tagCom.getBoolean("hasFluid")) {
				// System.out.println("Retrieving fluid");
				FluidStack storedFluid = new FluidStack(tagCom.getInteger("fluidID"),
						tagCom.getInteger("fluidAmount"));
				// System.out.println(storedFluid != null);
				// if (this.tank == null) System.out.println("ERROR! null tank");
				this.tank.setFluid(storedFluid);
			}
		}
		
	}
	
	protected void setSpecialRendererClass(Class<? extends TileEntitySpecialRenderer> clazz) {
		this.specialRendererClass = clazz;
	}
	
	public boolean hasSpecialRenderer() {
		return this.specialRendererClass != null;
	}
	
	public Class<? extends TileEntitySpecialRenderer> getSpecialRendererClass() {
		return this.specialRendererClass;
	}
	
	public void setPowered(boolean hasPower) {
		this.hasPower = hasPower;
	}
	
	@Override
	public void setRedstoneState(RedstoneState state) {
		this.redstoneState = state;
	}
	
	public boolean isPowered() {
		if (this.redstoneState == RedstoneState.IGNORE) {
			return true;
		}
		else if (this.redstoneState == RedstoneState.LOW) {
			return !this.hasPower;
		}
		else if (this.redstoneState == RedstoneState.HIGH) {
			return this.hasPower;
		}
		return false;
	}
	
	@Override
	public RedstoneState getRedstoneState() {
		return this.redstoneState;
	}
	
	public void getTileEntityDrops(ArrayList<ItemStack> drops) {
	}
	
	protected void setTank(FluidTank newTank) {
		this.tank = newTank;
	}
	
	public boolean canModifyTank() {
		return this.tank != null;
	}
	
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
	
	public FluidStack getFluidStack() {
		if (this.canModifyTank()) {
			if (this.tank.getFluidAmount() > 0) {
				return this.tank.getFluid().copy();
			}
		}
		return null;
	}
	
	public int getTankCapacity() {
		if (this.canModifyTank()) {
			return this.tank.getCapacity();
		}
		return 0;
	}
	
	public boolean loadTankFromNBT(NBTTagCompound tankTag) {
		if (this.canModifyTank()) {
			this.tank.readFromNBT(tankTag);
			return true;
		}
		return false;
	}
	
	public void clearTank() {
		this.tank.setFluid(null);
	}
	
	public void writeTankToNBT(NBTTagCompound tagCom) {
		this.tank.writeToNBT(tagCom);
	}
	
}
