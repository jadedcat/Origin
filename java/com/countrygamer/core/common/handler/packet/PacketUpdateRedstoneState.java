package com.countrygamer.core.common.handler.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.common.block.IRedstoneState;
import com.countrygamer.core.Base.common.packet.AbstractPacket;
import com.countrygamer.core.common.Core;
import com.countrygamer.core.common.lib.RedstoneState;

public class PacketUpdateRedstoneState extends AbstractPacket {
	
	int x, y, z;
	RedstoneState redstoneState;
	
	public PacketUpdateRedstoneState() {
	}
	
	public PacketUpdateRedstoneState(int x, int y, int z, RedstoneState state) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.redstoneState = state;
		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(RedstoneState.getIntFromState(this.redstoneState));
		
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		this.redstoneState = RedstoneState.getStateFromInt(buffer.readInt());
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.passStateToIRedstoneState(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.passStateToIRedstoneState(player);
	}
	
	private void passStateToIRedstoneState(EntityPlayer player) {
		Core.log.info("Recieved Redstone packet on " + (player.worldObj.isRemote ? "Client" : "Server"));
		TileEntity tileEnt = player.worldObj.getTileEntity(x, y, z);
		
		if (tileEnt != null && tileEnt instanceof IRedstoneState) {
			Core.log.info("Succesfully saved redstone");
			((IRedstoneState)tileEnt).setRedstoneState(this.redstoneState);
			
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
