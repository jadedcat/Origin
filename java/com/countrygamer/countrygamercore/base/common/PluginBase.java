package com.countrygamer.countrygamercore.base.common;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.countrygamer.countrygamercore.base.common.network.AbstractMessage;
import com.countrygamer.countrygamercore.base.extended.ExtendedEntity;
import com.countrygamer.countrygamercore.base.registry.PluginBiomeRegistry;
import com.countrygamer.countrygamercore.base.registry.PluginBlockRegistry;
import com.countrygamer.countrygamercore.base.registry.PluginEntityRegistry;
import com.countrygamer.countrygamercore.base.registry.PluginItemRegistry;
import com.countrygamer.countrygamercore.base.registry.PluginOptionRegistry;
import com.countrygamer.countrygamercore.common.network.PacketHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class PluginBase {
	
	private String pluginName = "";
	
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
		
	}
	
	protected void postInitialize(FMLPostInitializationEvent event) {
		
	}
}
