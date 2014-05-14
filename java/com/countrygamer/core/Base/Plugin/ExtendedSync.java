package com.countrygamer.core.Base.Plugin;

import java.util.HashMap;
import java.util.Map;

import com.countrygamer.countrygamercore.common.Core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtendedSync {
	
	private static Map<Class<? extends ExtendedEntity>, HashMap<String, NBTTagCompound>> persistanceTags = new HashMap<Class<? extends ExtendedEntity>, HashMap<String, NBTTagCompound>>();
	
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
				if (!shouldPersist)
					continue;
				
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
				if (!shouldPersist)
					continue;
				
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
							
							PacketSyncExtendedProperties packet = new PacketSyncExtendedProperties(
									extendedClass, extendedData);
							Core.instance.packetChannel.sendTo(packet,
									(EntityPlayerMP) player);
							Core.instance.packetChannel.sendToServer(packet);
							
							System.out.println("Loaded data for player "
									+ player.getGameProfile().getName()
									+ " for class " + extendedClass.getSimpleName());
						}
					}
				}
			}
		}
		
	}
	
}
