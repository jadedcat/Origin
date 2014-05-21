package com.countrygamer.core.Base.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.countrygamer.countrygamercore.common.Core;

public abstract class ExtendedEntity implements IExtendedEntityProperties {
	
	/**
	 * Holds data regarding IExtendedEntityProperties data and keys
	 * Key: Class which extends ExtendedEntity (which implements IExtendedEntityProperties)
	 * Valye: String array containing the key for that class in index 0, and whether or not to have
	 * these properties persist past death in index 1
	 */
	private static Map<Class<? extends ExtendedEntity>, String[]> extendedProperties = new HashMap<Class<? extends ExtendedEntity>, String[]>();
	
	public static final void registerExtended(String classKey,
			Class<? extends ExtendedEntity> extendedClass, boolean persistPastDeath) {
		ExtendedEntity.extendedProperties.put(extendedClass, new String[] {
				classKey, (persistPastDeath + "")
		});
	}
	
	public static final Map<Class<? extends ExtendedEntity>, String[]> getExtendedProperties() {
		return ExtendedEntity.extendedProperties;
	}
	
	public static final IExtendedEntityProperties getExtended(EntityPlayer player,
			Class<? extends ExtendedEntity> extendedClass) {
		return player.getExtendedProperties(ExtendedEntity.getExtendedProperties()
				.get(extendedClass)[0]);
	}
	
	public final EntityPlayer player;
	
	public ExtendedEntity(EntityPlayer player) {
		this.player = player;
		
	}
	
	public static final boolean regsiterPlayer(EntityPlayer player,
			Class<? extends ExtendedEntity> extendedClass) {
		ExtendedEntity ent = null;
		try {
			ent = extendedClass.getConstructor(EntityPlayer.class).newInstance(
					player);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		if (ent != null) {
			player.registerExtendedProperties(
					ExtendedEntity.extendedProperties.get(extendedClass)[0], ent);
			return true;
		}
		return false;
	}
	
	@Override
	public abstract void saveNBTData(NBTTagCompound compound);
	
	@Override
	public abstract void loadNBTData(NBTTagCompound compound);
	
	@Override
	public abstract void init(Entity entity, World world);
	
	public void syncEntity() {
		NBTTagCompound tagCom = new NBTTagCompound();
		this.saveNBTData(tagCom);
		
		PacketSyncExtendedProperties packet = new PacketSyncExtendedProperties(
				this.getClass(), tagCom);
		if (player instanceof EntityPlayerMP) {
			//System.out.println("Can sync from server to client");
			Core.instance.packetChannel.sendTo(packet, (EntityPlayerMP) player);
		}
		else {
			//System.out.println("Cannot sync from server, not EntityPlayerMP");
		}
		Core.instance.packetChannel.sendToServer(packet);
	}
	
}
