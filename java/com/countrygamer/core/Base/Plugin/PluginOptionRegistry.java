package com.countrygamer.core.Base.Plugin;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface PluginOptionRegistry {
	
	public boolean hasCustomConfiguration();
	
	public void customizeConfiguration(FMLPreInitializationEvent event);
	
	public void registerOptions(Configuration config);
	
}
