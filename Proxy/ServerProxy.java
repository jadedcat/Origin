package com.countrygamer.countrygamer_core.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.countrygamer.countrygamer_core.Handler.CoreEventHandler;

public class ServerProxy {
	
	public void preInit() {
	}
	public void registerRender() {
		
	}
	public void registerHandler() {
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
	}
	
}
