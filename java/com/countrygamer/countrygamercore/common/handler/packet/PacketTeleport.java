package com.countrygamer.countrygamercore.common.handler.packet;

import com.countrygamer.core.Base.common.packet.AbstractPacket;
import com.countrygamer.countrygamercore.lib.CoreUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketTeleport extends AbstractPacket {

	private int dimId = 0;
	private double[] coords = new double[3];
	private boolean fallDamage, particles;

	public PacketTeleport() {

	}

	public PacketTeleport(int dim, double[] coords) {
		this(dim, coords, false, false);
	}
	public PacketTeleport(int dim, double[] coords, boolean fall, boolean particles) {
		this.dimId = dim;
		this.coords = coords;
		this.fallDamage = fall;
		this.particles = particles;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.dimId);
		buffer.writeDouble(coords[0]);
		buffer.writeDouble(coords[1]);
		buffer.writeDouble(coords[2]);
		buffer.writeBoolean(this.particles);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.dimId = buffer.readInt();
		this.coords[0] = buffer.readDouble();
		this.coords[1] = buffer.readDouble();
		this.coords[2] = buffer.readDouble();
		this.particles = buffer.readBoolean();
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//System.out.println("Client PacketTeleport Recieved");
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//System.out.println("Server PacketTeleport Recieved");
		CoreUtil.teleportPlayerToDimension(player, this.dimId);
		CoreUtil.teleportPlayer(player, coords[0], coords[1], coords[2], fallDamage, particles);
	}

}
