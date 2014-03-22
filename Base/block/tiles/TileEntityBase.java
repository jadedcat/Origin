package com.countrygamer.core.Base.block.tiles;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

	private Class<? extends TileEntitySpecialRenderer> specialRendererClass = null;

	public TileEntityBase() {
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
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
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

}
