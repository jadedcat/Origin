package com.countrygamer.countrygamercore.Base.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class MessageTileEntityBase extends AbstractMessage {
	
	int x, y, z;
	
	public MessageTileEntityBase() {
	}
	
	public MessageTileEntityBase(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	@Override
	public void writeTo(ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		
	}
	
	@Override
	public void readFrom(ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		
	}
	
	@Override
	public void handleOnClient(EntityPlayer player) {
		this.handleSync(player, player.worldObj.getTileEntity(this.x, this.y, this.z));
	}
	
	@Override
	public void handleOnServer(EntityPlayer player) {
		this.handleSync(player, player.worldObj.getTileEntity(this.x, this.y, this.z));
	}
	
	protected void handleSync(EntityPlayer player, TileEntity tileEntity) {
		
	}
	
}
