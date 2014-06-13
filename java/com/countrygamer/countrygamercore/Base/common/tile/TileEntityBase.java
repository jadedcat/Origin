package com.countrygamer.countrygamercore.Base.common.tile;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.countrygamercore.Base.common.block.IRedstoneState;
import com.countrygamer.countrygamercore.lib.RedstoneState;
import com.countrygamer.countrygamercore.lib.UtilDrops;

/**
 * A very basic tile entity
 * 
 * @author Country_Gamer
 * 
 */
public class TileEntityBase extends TileEntity implements IRedstoneState {
	
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
		
		this.setRedstoneState(RedstoneState.HIGH);
		
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
		tagCom.setInteger("redstoneStateID", RedstoneState.getIntFromState(this.redstoneState));
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		this.name = tagCom.getString("teName");
		
		this.isRecievingPower = tagCom.getBoolean("base_hasPower");
		this.redstoneState = RedstoneState.getStateFromInt(tagCom.getInteger("redstoneStateID"));
		
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
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~ Setting and Getting this tiles power ~~~~~~~~~~~~~~~~~~~~
	public void setPowered(boolean hasPower) {
		this.isRecievingPower = hasPower;
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
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~ Override methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void invalidate() {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		this.getTileEntityDrops(drops);
		UtilDrops.spawnDrops(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, drops);
		super.invalidate();
	}
	
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