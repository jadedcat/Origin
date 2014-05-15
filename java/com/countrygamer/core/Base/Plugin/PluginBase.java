package com.countrygamer.core.Base.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import com.countrygamer.core.Base.common.packet.AbstractPacket;
import com.countrygamer.core.Base.common.packet.PacketPipeline;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class PluginBase {
	
	private String pluginName = "";
	private ArrayList<Class<? extends AbstractPacket>> packets = new ArrayList<Class<? extends AbstractPacket>>();
	
	protected PluginOptionRegistry options = null;
	protected PluginItemRegistry itemReg = null;
	protected PluginBlockRegistry blockReg = null;
	protected PluginEntityRegistry entityReg = null;
	protected PluginBiomeRegistry biomeReg = null;
	
	public final PacketPipeline packetChannel = new PacketPipeline();
	
	protected void preInitialize(String pluginName, FMLPreInitializationEvent event,
			PluginCommonProxy proxy, PluginOptionRegistry optionRegistry,
			PluginItemRegistry itemRegistry, PluginBlockRegistry blockRegistry,
			PluginBiomeRegistry biomeRegistry, PluginEntityRegistry entityRegistry) {
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
				File cfgFile = new File(event.getModConfigurationDirectory(),
						this.pluginName + ".cfg");
				Configuration config = new Configuration(cfgFile, true);
				config.load();
				this.options.registerOptions(config);
				config.save();
			}
		}
		
		proxy.registerRender();
		
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
		
		this.registerHandlers(null, this, null, null);
		this.registerHandlers(null, new ExtendedSync(), null, null);
		this.registerPacketClass(PacketSyncExtendedProperties.class);
	}
	
	protected void registerHandlers(Object plugin, Object eventHandler,
			IFuelHandler fuelHandler, IGuiHandler guiHandler) {
		if (eventHandler != null) MinecraftForge.EVENT_BUS.register(eventHandler);
		if (fuelHandler != null) GameRegistry.registerFuelHandler(fuelHandler);
		if (plugin != null && guiHandler != null)
			NetworkRegistry.INSTANCE.registerGuiHandler(plugin, guiHandler);
	}
	
	protected void registerExtendedPlayer(String classKey,
			Class<? extends ExtendedEntity> extendedClass, boolean persistPastDeath) {
		ExtendedEntity.registerExtended(classKey, extendedClass, persistPastDeath);
	}
	
	protected void registerPacketClass(Class<? extends AbstractPacket> packetClass) {
		this.packets.add(packetClass);
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
		this.packetChannel.initalise(this.pluginName);
		for (int i = 0; i < this.packets.size(); i++) {
			this.packetChannel.registerPacket(this.packets.get(i));
		}
		
	}
	
	protected void postInitialize(FMLPostInitializationEvent event) {
		this.packetChannel.postInitialise();
		
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity != null && event.entity instanceof EntityPlayer) {
			Map<Class<? extends ExtendedEntity>, String[]> extendedProperties = ExtendedEntity
					.getExtendedProperties();
			for (Class<? extends ExtendedEntity> extendedClass : extendedProperties
					.keySet()) {
				
				IExtendedEntityProperties props = event.entity
						.getExtendedProperties(extendedProperties.get(extendedClass)[0]);
				if (props == null) {
					ExtendedEntity.regsiterPlayer((EntityPlayer) event.entity,
							extendedClass);
				}
				
				if (!event.entity.worldObj.isRemote) {
					props = event.entity.getExtendedProperties(extendedProperties
							.get(extendedClass)[0]);
					ExtendedEntity extendedEnt = (ExtendedEntity) props;
					extendedEnt.onPropertyChanged(extendedEnt);
				}
			}
		}
	}
	
}
