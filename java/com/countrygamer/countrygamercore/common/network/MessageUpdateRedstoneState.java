package com.countrygamer.countrygamercore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.common.block.IRedstoneState;
import com.countrygamer.core.Base.common.network.AbstractMessage;
import com.countrygamer.countrygamercore.common.Core;
import com.countrygamer.countrygamercore.lib.RedstoneState;

public class MessageUpdateRedstoneState extends AbstractMessage {
	
	int x, y, z;
	RedstoneState redstoneState;
	
	public MessageUpdateRedstoneState() {
	}
	
	public MessageUpdateRedstoneState(int x, int y, int z, RedstoneState state) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.redstoneState = state;
		
	}
	
	@Override
	public void writeTo(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(RedstoneState.getIntFromState(this.redstoneState));
		
	}
	
	@Override
	public void readFrom(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		this.redstoneState = RedstoneState.getStateFromInt(buffer.readInt());
		
	}
	
	@Override
	public void handleOnClient(EntityPlayer player) {
		this.passStateToIRedstoneState(player);
	}
	
	@Override
	public void handleOnServer(EntityPlayer player) {
		this.passStateToIRedstoneState(player);
	}
	
	private void passStateToIRedstoneState(EntityPlayer player) {
		Core.logger.info("Recieved Redstone packet on "
				+ (player.worldObj.isRemote ? "Client" : "Server"));
		TileEntity tileEnt = player.worldObj.getTileEntity(x, y, z);
		
		if (tileEnt != null && tileEnt instanceof IRedstoneState) {
			Core.logger.info("Succesfully saved redstone");
			((IRedstoneState) tileEnt).setRedstoneState(this.redstoneState);
			
			Block block = player.worldObj.getBlock(x, y, z);
			player.worldObj.notifyBlockOfNeighborChange(x + 1, y + 0, z + 0, block);
			player.worldObj.notifyBlockOfNeighborChange(x - 1, y + 0, z + 0, block);
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 1, z + 0, block);
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y - 1, z + 0, block);
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 0, z + 1, block);
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 0, z - 1, block);
			
		}
	}
	
}
