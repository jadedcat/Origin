package com.countrygamer.core.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.countrygamer.core.Handler.CoreEventHandler;

public class ServerProxy {
	
	public void preInit() {
	}
	public void registerRender() {
		
	}
	public void registerHandler() {
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
	}
	
}
