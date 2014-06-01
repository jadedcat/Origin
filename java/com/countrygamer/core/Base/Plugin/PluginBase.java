package com.countrygamer.core.Base.Plugin;

import java.io.File;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.countrygamer.core.Base.Plugin.extended.ExtendedEntity;
import com.countrygamer.core.Base.Plugin.extended.ExtendedSync;
import com.countrygamer.core.Base.Plugin.registry.PluginBiomeRegistry;
import com.countrygamer.core.Base.Plugin.registry.PluginBlockRegistry;
import com.countrygamer.core.Base.Plugin.registry.PluginEntityRegistry;
import com.countrygamer.core.Base.Plugin.registry.PluginItemRegistry;
import com.countrygamer.core.Base.Plugin.registry.PluginOptionRegistry;
import com.countrygamer.core.Base.common.network.AbstractMessage;
import com.countrygamer.core.Base.common.network.PacketHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class PluginBase {
	
	private String pluginID = "", pluginName = "";
	// private ArrayList<Class<? extends AbstractPacket>> packets = new ArrayList<Class<? extends
	// AbstractPacket>>();
	
	protected PluginOptionRegistry options = null;
	protected PluginItemRegistry itemReg = null;
	protected PluginBlockRegistry blockReg = null;
	protected PluginEntityRegistry entityReg = null;
	protected PluginBiomeRegistry biomeReg = null;
	
	protected void preInitialize(String pluginID, String pluginName,
			FMLPreInitializationEvent event, PluginCommonProxy proxy,
			PluginOptionRegistry optionRegistry, PluginItemRegistry itemRegistry,
			PluginBlockRegistry blockRegistry, PluginBiomeRegistry biomeRegistry,
			PluginEntityRegistry entityRegistry) {
		this.pluginID = pluginID;
		this.pluginName = pluginName;
		this.options = optionRegistry;
		this.itemReg = itemRegistry;
		this.blockReg = blockRegistry;
		this.entityReg = entityRegistry;
		this.biomeReg = biomeRegistry;
		
		if (this.options != null) {
			if (this.options.hasCustomConfiguration())
				this.options.customizeConfiguration(event);
			else {
				File cfgFile = new File(event.getModConfigurationDirectory(), this.pluginName
						+ ".cfg");
				Configuration config = new Configuration(cfgFile, true);
				config.load();
				this.options.registerOptions(config);
				config.save();
			}
		}
		
		if (this.itemReg != null) this.itemReg.registerItems();
		if (this.blockReg != null) {
			this.blockReg.registryTileEntities();
			this.blockReg.registerBlocks();
		}
		if (this.itemReg != null) {
			this.itemReg.registerItemsPostBlock();
			this.itemReg.registerItemCraftingRecipes();
			this.itemReg.registerItemSmeltingRecipes();
			this.itemReg.registerOtherItemRecipes();
		}
		if (this.blockReg != null) {
			this.blockReg.registerBlockCraftingRecipes();
			this.blockReg.registerBlockSmeltingRecipes();
			this.blockReg.registerOtherBlockRecipes();
		}
		if (this.biomeReg != null) {
			this.biomeReg.registerBiomes();
			this.biomeReg.registerBiomesWithWorldGen();
		}
		if (this.entityReg != null) {
			this.entityReg.registerEntities();
			this.entityReg.addEntityMappings();
			this.entityReg.registerEntitySpawns();
		}
		
		proxy.registerRender();
		
		this.registerHandlers(this, null);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		
	}
	
	protected void registerHandlers(Object eventHandler, IFuelHandler fuelHandler) {
		if (eventHandler != null) {
			MinecraftForge.EVENT_BUS.register(eventHandler);
			FMLCommonHandler.instance().bus().register(eventHandler);
		}
		if (fuelHandler != null) GameRegistry.registerFuelHandler(fuelHandler);
	}
	
	protected void regsiterPacketHandler(String pluginID,
			Class<? extends AbstractMessage>... messages) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			PacketHandler.registerHandler(pluginID, messages);
		}
	}
	
	protected void registerExtendedPlayer(String classKey,
			Class<? extends ExtendedEntity> extendedClass, boolean persistPastDeath) {
		ExtendedEntity.registerExtended(classKey, extendedClass, persistPastDeath);
	}
	
	@Mod.EventHandler
	public void imcHandler(FMLInterModComms.IMCEvent event) {
		for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
			if (imcMessage.key.startsWith("PluginBase:")) {
				if (imcMessage.key.endsWith("register_extended_entity")) {
					
				}
			}
		}
	}
	
	protected void initialize(FMLInitializationEvent event) {
		/*
		this.packetChannel.initalise(this.pluginName);
		for (int i = 0; i < this.packets.size(); i++) {
			this.packetChannel.registerPacket(this.packets.get(i));
		}
		 */
		
	}
	
	protected void postInitialize(FMLPostInitializationEvent event) {
		// this.packetChannel.postInitialise();
		
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
