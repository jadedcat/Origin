package com.countrygamer.core.Base.common.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.common.block.IRedstoneState;
import com.countrygamer.core.common.lib.RedstoneState;

public class TileEntityBase extends TileEntity implements IRedstoneState {
	
	private Class<? extends TileEntitySpecialRenderer> specialRendererClass = null;
	private boolean hasPower = false;
	private RedstoneState redstoneState;
	
	public TileEntityBase() {
		this.setRedstoneState(RedstoneState.HIGH);
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
		tagCom.setInteger("redstoneStateID",
				RedstoneState.getIntFromState(this.redstoneState));
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.hasPower = tagCom.getBoolean("base_hasPower");
		this.redstoneState = RedstoneState.getStateFromInt(tagCom
				.getInteger("redstoneStateID"));
		
	}
	
	protected void setSpecialRendererClass(
			Class<? extends TileEntitySpecialRenderer> clazz) {
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
	
}
