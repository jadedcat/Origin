package com.countrygamer.countrygamercore.Base.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractMessage {
	
	public void writeTo_do(ByteBuf buffer) {
		this.writeTo(buffer);
	}
	
	public abstract void writeTo(ByteBuf buffer);
	
	/*
	public void readFrom_do(ByteBuf buffer, EntityPlayer player, Side side) {
		this.readFrom(buffer);
		if (side.isClient()) {
			this.handleOnClient(player);
		}
		else if (side.isServer()) {
			this.handleOnServer(player);
		}
	}
	*/
	
	public abstract void readFrom(ByteBuf buffer);
	
	public abstract void handleOnClient(EntityPlayer player);
	
	public abstract void handleOnServer(EntityPlayer player);
	
}
