package com.countrygamer.core.Base.Plugin;

public interface PluginBlockRegistry {
	
	public void registryTileEntities();
	
	public void registerBlocks();
	
	public void registerBlockCraftingRecipes();
	
	public void registerBlockSmeltingRecipes();
	
	public void registerOtherBlockRecipes();
	
}
