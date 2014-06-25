package com.countrygamer.countrygamercore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.countrygamercore.base.common.network.AbstractMessage;
import com.countrygamer.countrygamercore.base.common.tile.IActivity;
import com.countrygamer.countrygamercore.common.lib.Activity;

public class MessageUpdateActivity extends AbstractMessage {
	
	int x, y, z;
	Activity activityState;
	
	public MessageUpdateActivity() {
	}
	
	public MessageUpdateActivity(int x, int y, int z, Activity state) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.activityState = state;
		
	}
	
	@Override
	public void writeTo(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(Activity.getInt(this.activityState));
		
	}
	
	@Override
	public void readFrom(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		this.activityState = Activity.getState(buffer.readInt());
		
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
		// Core.logger.info("Recieved Redstone packet on "
		// + (player.worldObj.isRemote ? "Client" : "Server"));
		TileEntity tileEnt = player.worldObj.getTileEntity(x, y, z);
		
		if (tileEnt != null && tileEnt instanceof IActivity) {
			// Core.logger.info("Succesfully saved redstone");
			((IActivity) tileEnt).setActivity(this.activityState);
			
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
