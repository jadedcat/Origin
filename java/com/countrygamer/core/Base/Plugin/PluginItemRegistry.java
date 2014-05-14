package com.countrygamer.core.Base.Plugin;

public interface PluginItemRegistry {
	
	public void registerItems();
	
	public void registerItemsPostBlock();
	
	public void registerItemCraftingRecipes();
	
	public void registerItemSmeltingRecipes();
	
	public void registerOtherItemRecipes();
	
}
