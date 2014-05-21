package com.countrygamer.core.Base.Plugin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.countrygamer.core.Base.common.packet.AbstractPacket;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketSyncExtendedProperties extends AbstractPacket {
	
	Class<? extends ExtendedEntity> extendedClass;
	NBTTagCompound data;
	
	public PacketSyncExtendedProperties() {
	}
	
	public PacketSyncExtendedProperties(
			Class<? extends ExtendedEntity> extendedClass, NBTTagCompound entityData) {
		this.data = entityData;
		this.extendedClass = extendedClass;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteBufUtils.writeTag(buffer, data);
		ByteBufUtils.writeUTF8String(buffer, ExtendedEntity.getExtendedProperties()
				.get(this.extendedClass)[0]);
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.data = ByteBufUtils.readTag(buffer);
		String key = ByteBufUtils.readUTF8String(buffer);
		// System.out.println(key);
		this.extendedClass = this.getClassWithKey(key);
	}
	
	Class<? extends ExtendedEntity> getClassWithKey(String classKey) {
		for (Class<? extends ExtendedEntity> extendedClass : ExtendedEntity
				.getExtendedProperties().keySet()) {
			if (ExtendedEntity.getExtendedProperties().get(extendedClass)[0]
					.equals(classKey)) {
				return extendedClass;
			}
		}
		return null;
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.syncData(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.syncData(player);
	}
	
	void syncData(EntityPlayer player) {
		//System.out.println("Recieved " + this.getClass().getSimpleName() + " on "
		//		+ (player.worldObj.isRemote ? "Client" : "Server"));
		String[] extendedClassKeys = ExtendedEntity.getExtendedProperties().get(
				this.extendedClass);
		String classKey = extendedClassKeys[0];
		IExtendedEntityProperties props = player.getExtendedProperties(classKey);
		ExtendedEntity extendedPlayer = this.extendedClass.cast(props);
		extendedPlayer.loadNBTData(this.data);
		
	}
	
}
