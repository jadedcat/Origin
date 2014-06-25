package com.countrygamer.countrygamercore.common.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.countrygamer.countrygamercore.base.extended.ExtendedEntity;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
	
	/**
	 * Control the creation of ExtendedEntities for each player (only if they do not already have
	 * that EntendedEntity)
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity != null && event.entity instanceof EntityPlayer) {
			Map<Class<? extends ExtendedEntity>, String[]> extendedProperties = ExtendedEntity
					.getExtendedProperties();
			for (Class<? extends ExtendedEntity> extendedClass : extendedProperties.keySet()) {
				
				IExtendedEntityProperties props = event.entity
						.getExtendedProperties(extendedProperties.get(extendedClass)[0]);
				if (props == null) {
					ExtendedEntity.registerPlayer((EntityPlayer) event.entity, extendedClass);
				}
				
				if (!event.entity.worldObj.isRemote) {
					props = event.entity.getExtendedProperties(extendedProperties
							.get(extendedClass)[0]);
					// ((ExtendedEntity) props).syncEntity();
				}
			}
		}
	}
	
	/**
	 * Handles the saving of ExtendedEntity data when an entity dies
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.entity == null || event.entity.worldObj.isRemote
				|| !(event.entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) event.entity;
		
		Map<Class<? extends ExtendedEntity>, String[]> propertyMap = ExtendedEntity
				.getExtendedProperties();
		// Get each and ever possible ExtendedEntity
		for (Class<? extends ExtendedEntity> extendedClass : propertyMap.keySet()) {
			// Get the boolean associated with each ExtendedEntity, regarding whether or not is
			// should persist past a death event
			String shouldPersist = propertyMap.get(extendedClass)[1];
			// Check for persistance
			if (Boolean.parseBoolean(shouldPersist)) {
				// Get the player's ExtendedEntity instance for this ExtendedEntity class
				ExtendedEntity extendedPlayer = (ExtendedEntity) ExtendedEntity.getExtended(player,
						extendedClass);
				
				// Create a new tag compound for data storage
				NBTTagCompound extPlayerData = new NBTTagCompound();
				// Save the ExtendedEntity instance's data
				extendedPlayer.saveNBTData(extPlayerData);
				
				// Store it for persistance
				ExtendedSync.storeEntityData(extendedClass, player, extPlayerData);
			}
		}
	}
	
	/**
	 * Handles retrieving a player's ExtendedEntity data, after they have died.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity == null || event.entity.worldObj.isRemote
				|| !(event.entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) event.entity;
		
		Map<Class<? extends ExtendedEntity>, String[]> propertyMap = ExtendedEntity
				.getExtendedProperties();
		// Get each and ever possible ExtendedEntity
		for (Class<? extends ExtendedEntity> extendedClass : propertyMap.keySet()) {
			// Get the boolean associated with each ExtendedEntity, regarding whether or not is
			// should persist past a death event
			String shouldPersist = propertyMap.get(extendedClass)[1];
			
			// Get the player's ExtendedEntity instance for this ExtendedEntity class
			ExtendedEntity extendedPlayer = (ExtendedEntity) ExtendedEntity.getExtended(player,
					extendedClass);
			
			if (extendedPlayer == null) continue;
			
			// Check for persistance
			if (Boolean.parseBoolean(shouldPersist)) {
				// Get the any data from the persistance storage
				NBTTagCompound extPlayerData = ExtendedSync.getEntityData(extendedClass, player);
				// Make sure it was stored
				if (extPlayerData != null) {
					
					// Load the data back to the player
					extendedPlayer.loadNBTData(extPlayerData);
					
				}
			}
			// Make sure to sync the player so that the data is not lost.
			// This was all saved and loaded Server side, so now it must be loaded Client
			// side
			extendedPlayer.syncEntity();
		}
	}
	
}
