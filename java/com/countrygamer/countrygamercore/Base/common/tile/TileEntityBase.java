package com.countrygamer.countrygamercore.base.common.tile;

import java.util.ArrayList;

import com.countrygamer.countrygamercore.common.lib.Activity;
import com.countrygamer.countrygamercore.common.lib.RedstoneState;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * A very basic tile entity
 * 
 * @author Country_Gamer
 * 
 */
public class TileEntityBase extends TileEntity implements IRedstoneState, IActivity {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Global Variables ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * This tile entity's name
	 */
	private String name;
	
	/**
	 * Holds the current state of this tile entity
	 */
	private RedstoneState redstoneState;
	private Activity activityState;
	/**
	 * Hold whether or not this tile is recieving power
	 */
	private boolean isRecievingPower = false;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Constructor (Empty)
	 */
	public TileEntityBase(String name) {
		super();
		this.name = name;
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Syncing the tile ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~ Saving and Loading ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		tagCom.setString("teName", this.name);
		
		tagCom.setBoolean("base_hasPower", this.isRecievingPower);
		if (this.redstoneState == null) this.redstoneState = RedstoneState.HIGH;
		tagCom.setInteger("redstoneStateID", RedstoneState.getIntFromState(this.redstoneState));
		if (this.activityState == null) this.activityState = Activity.PULSE;
		tagCom.setInteger("activityState_ID", Activity.getInt(this.activityState));
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		this.name = tagCom.getString("teName");
		
		this.isRecievingPower = tagCom.getBoolean("base_hasPower");
		this.redstoneState = RedstoneState.getStateFromInt(tagCom.getInteger("redstoneStateID"));
		this.activityState = Activity.getState(tagCom.getInteger("activityState_ID"));
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~ Getter for Name ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~ Redstone State ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Used to set the current redstone state of this tile entity
	 */
	@Override
	public void setRedstoneState(RedstoneState state) {
		this.redstoneState = state;
	}
	
	/**
	 * Used to get the current redstone state of this tile entity
	 */
	@Override
	public RedstoneState getRedstoneState() {
		return this.redstoneState;
	}
	
	@Override
	public void setActivity(Activity activity) {
		this.activityState = activity;
	}
	
	@Override
	public Activity getActivity() {
		return this.activityState;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Setting and Getting this tiles power ~~~~~~~~~~~~~~~~~~~~
	public void setPowered(boolean hasPower) {
		this.isRecievingPower = hasPower;
		this.onPowerChanged();
		
	}
	
	public boolean isPowered() {
		if (this.redstoneState == RedstoneState.IGNORE) {
			return true;
		}
		else if (this.redstoneState == RedstoneState.LOW) {
			return !this.isRecievingPower;
		}
		else if (this.redstoneState == RedstoneState.HIGH) {
			return this.isRecievingPower;
		}
		return false;
	}
	
	public void onPowerChanged() {
	}
	
	public boolean canRun() {
		return (this.getRedstoneState() == RedstoneState.IGNORE)
				|| (this.getRedstoneState() == RedstoneState.HIGH && this.isPowered())
				|| (this.getRedstoneState() == RedstoneState.LOW && !this.isPowered());
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~ Override methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Override this in order to modify what is dropped then this tile entity breaks
	 * 
	 * @param drops
	 */
	public void getTileEntityDrops(ArrayList<ItemStack> drops) {
		Block block = this.getWorldObj().getBlock(this.xCoord, this.yCoord, this.zCoord);
		int meta = this.getWorldObj().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		drops.add(new ItemStack(block, 1, meta));
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
}
