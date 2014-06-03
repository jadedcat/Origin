package com.countrygamer.countrygamercore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.countrygamercore.Base.common.network.AbstractMessage;
import com.countrygamer.countrygamercore.lib.CoreUtil;

public class MessageTeleport extends AbstractMessage {
	
	private int dimId = 0;
	private double[] coords = new double[3];
	private boolean fallDamage, particles;
	
	public MessageTeleport() {
		
	}
	
	public MessageTeleport(int dim, double[] coords) {
		this(dim, coords, false, false);
	}
	
	public MessageTeleport(int dim, double[] coords, boolean fall, boolean particles) {
		this.dimId = dim;
		this.coords = coords;
		this.fallDamage = fall;
		this.particles = particles;
	}
	
	@Override
	public void writeTo(ByteBuf buffer) {
		buffer.writeInt(this.dimId);
		buffer.writeDouble(coords[0]);
		buffer.writeDouble(coords[1]);
		buffer.writeDouble(coords[2]);
		buffer.writeBoolean(this.particles);
		
	}
	
	@Override
	public void readFrom(ByteBuf buffer) {
		this.dimId = buffer.readInt();
		this.coords[0] = buffer.readDouble();
		this.coords[1] = buffer.readDouble();
		this.coords[2] = buffer.readDouble();
		this.particles = buffer.readBoolean();
		
	}
	
	@Override
	public void handleOnClient(EntityPlayer player) {
	}
	
	@Override
	public void handleOnServer(EntityPlayer player) {
		CoreUtil.teleportPlayerToDimension(player, this.dimId);
		CoreUtil.teleportPlayer(player, coords[0], coords[1], coords[2], fallDamage, particles);
	}
	
}
