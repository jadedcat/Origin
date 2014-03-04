package com.countrygamer.core;

import java.util.logging.Logger;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MC_VERSION)
public class ModClassBase {

	public static Logger log = Logger.getLogger("");

	@EventHandler
	/**
	 * Before doing things with the mod.
	 * Do everything in here, unless it has to do with compatibility with other mods.
	 * @param event
	 */
	public void preInit(FMLPreInitializationEvent event) {
		this.doPreProxyThings();
		this.setupConfig(event);
		this.doProxyThings();
		this.registerHandlers();
		this.registerItems();
		this.registerBlocks();
		this.registerCraftingRecipes();
		this.registerSmeltingRecipes();
		this.registerEntities();
		this.biomes();

	}

	public void doPreProxyThings() {
	}

	public void setupConfig(FMLPreInitializationEvent event) {
	}

	public void doProxyThings() {
	}

	public void registerHandlers() {

	}

	public void registerItems() {
		this.registerArmor();
	}

	public void registerArmor() {
	}

	public void registerBlocks() {
	}

	public void registerCraftingRecipes() {
	}

	public void registerSmeltingRecipes() {
	}

	public void registerEntities() {
	}

	public void biomes() {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

}
