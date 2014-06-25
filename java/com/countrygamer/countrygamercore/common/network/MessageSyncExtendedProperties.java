package com.countrygamer.countrygamercore.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.countrygamer.countrygamercore.base.common.network.AbstractMessage;
import com.countrygamer.countrygamercore.base.extended.ExtendedEntity;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageSyncExtendedProperties extends AbstractMessage {
	
	Class<? extends ExtendedEntity> extendedClass;
	NBTTagCompound data;
	
	public MessageSyncExtendedProperties() {
	}
	
	public MessageSyncExtendedProperties(Class<? extends ExtendedEntity> extendedClass,
			NBTTagCompound entityData) {
		this.data = entityData;
		this.extendedClass = extendedClass;
	}
	
	@Override
	public void writeTo(ByteBuf buffer) {
		ByteBufUtils.writeTag(buffer, data);
		ByteBufUtils.writeUTF8String(buffer,
				ExtendedEntity.getExtendedProperties().get(this.extendedClass)[0]);
		
	}
	
	@Override
	public void readFrom(ByteBuf buffer) {
		this.data = ByteBufUtils.readTag(buffer);
		String key = ByteBufUtils.readUTF8String(buffer);
		// System.out.println(key);
		this.extendedClass = this.getClassWithKey(key);
		
	}
	
	Class<? extends ExtendedEntity> getClassWithKey(String classKey) {
		for (Class<? extends ExtendedEntity> extendedClass : ExtendedEntity.getExtendedProperties()
				.keySet()) {
			if (ExtendedEntity.getExtendedProperties().get(extendedClass)[0].equals(classKey)) {
				return extendedClass;
			}
		}
		return null;
	}
	
	@Override
	public void handleOnClient(EntityPlayer player) {
		this.syncData(player);
		
	}
	
	@Override
	public void handleOnServer(EntityPlayer player) {
		this.syncData(player);
		
	}
	
	void syncData(EntityPlayer player) {
		// System.out.println("Recieved " + this.getClass().getSimpleName() + " on "
		// + (player.worldObj.isRemote ? "Client" : "Server"));
		String[] extendedClassKeys = ExtendedEntity.getExtendedProperties().get(this.extendedClass);
		String classKey = extendedClassKeys[0];
		IExtendedEntityProperties props = player.getExtendedProperties(classKey);
		ExtendedEntity extendedPlayer = this.extendedClass.cast(props);
		extendedPlayer.loadNBTData(this.data);
		
	}
	
}
