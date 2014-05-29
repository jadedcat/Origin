package com.countrygamer.core.Base.Plugin.extended;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ExtendedSync {
	
	private static Map<Class<? extends ExtendedEntity>, HashMap<String, NBTTagCompound>> persistanceTags = new HashMap<Class<? extends ExtendedEntity>, HashMap<String, NBTTagCompound>>();
	
	public static void storeEntityData(
			Class<? extends ExtendedEntity> extendedClass, EntityPlayer player,
			NBTTagCompound data) {
		ExtendedSync.checkForClassKey(extendedClass);
		String playerName = player.getGameProfile().getName();
		ExtendedSync.persistanceTags.get(extendedClass).put(playerName, data);
	}
	
	public static NBTTagCompound getEntityData(
			Class<? extends ExtendedEntity> extendedClass, EntityPlayer player) {
		if (ExtendedSync.checkForClassKey(extendedClass)) {
			String playerName = player.getGameProfile().getName();
			return ExtendedSync.persistanceTags.get(extendedClass).remove(playerName);
		}
		return null;
	}
	
	private static boolean checkForClassKey(
			Class<? extends ExtendedEntity> extendedClass) {
		if (!ExtendedSync.persistanceTags.containsKey(extendedClass)) {
			ExtendedSync.persistanceTags.put(extendedClass,
					new HashMap<String, NBTTagCompound>());
			return false;
		}
		return true;
	}
	
	/*
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.entity != null && !event.entity.worldObj.isRemote
				&& event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			
			Map<Class<? extends ExtendedEntity>, String[]> classKeyMap = ExtendedEntity
					.getExtendedProperties();
			
			for (Class<? extends ExtendedEntity> extendedClass : classKeyMap
					.keySet()) {
				if (!ExtendedSync.persistanceTags.containsKey(extendedClass))
					ExtendedSync.persistanceTags.put(extendedClass,
							new HashMap<String, NBTTagCompound>());
				
				boolean shouldPersist = Boolean.parseBoolean(classKeyMap
						.get(extendedClass)[1]);
				if (!shouldPersist) continue;
				
				String classKey = classKeyMap.get(extendedClass)[0];
				
				IExtendedEntityProperties props = player
						.getExtendedProperties(classKey);
				if (props != null) {
					if (props.getClass().isAssignableFrom(extendedClass)) {
						ExtendedEntity extendedPlayer = extendedClass.cast(props);
						NBTTagCompound extendedData = new NBTTagCompound();
						extendedPlayer.saveNBTData(extendedData);
						
						ExtendedSync.persistanceTags.get(extendedClass).put(
								player.getGameProfile().getName(), extendedData);
						
						System.out.println("Saved data for player "
								+ player.getGameProfile().getName()
								+ " for extended entity "
								+ extendedClass.getSimpleName());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity != null && !event.entity.worldObj.isRemote
				&& event.entity instanceof EntityPlayer) {
			// System.out.println("Entity Join");
			EntityPlayer player = (EntityPlayer) event.entity;
			
			Map<Class<? extends ExtendedEntity>, String[]> classKeyMap = ExtendedEntity
					.getExtendedProperties();
			
			for (Class<? extends ExtendedEntity> extendedClass : classKeyMap
					.keySet()) {
				if (!ExtendedSync.persistanceTags.containsKey(extendedClass))
					continue;
				
				boolean shouldPersist = Boolean.parseBoolean(classKeyMap
						.get(extendedClass)[1]);
				if (!shouldPersist) continue;
				
				String classKey = classKeyMap.get(extendedClass)[0];
				
				IExtendedEntityProperties props = player
						.getExtendedProperties(classKey);
				if (props != null) {
					if (props.getClass().isAssignableFrom(extendedClass)) {
						ExtendedEntity extendedPlayer = extendedClass.cast(props);
						
						if (ExtendedSync.persistanceTags.get(extendedClass)
								.containsKey(player.getGameProfile().getName())) {
							NBTTagCompound extendedData = ExtendedSync.persistanceTags
									.get(extendedClass).remove(
											player.getGameProfile().getName());
							
							extendedPlayer.loadNBTData(extendedData);
							
							// extendedPlayer.onPropertyChanged(extendedPlayer);
							PacketSyncExtendedProperties packet = new PacketSyncExtendedProperties(
									extendedClass, extendedData);
							if (player instanceof EntityPlayerMP)
								Core.instance.packetChannel.sendTo(packet,
										(EntityPlayerMP) player);
							// Core.instance.packetChannel.sendToServer(packet);
							
							System.out.println("Loaded data for player "
									+ player.getGameProfile().getName()
									+ " for class " + extendedClass.getSimpleName());
						}
					}
				}
			}
		}
		
	}
	
	*/
}
