package com.countrygamer.countrygamercore.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.countrygamer.countrygamercore.Base.Plugin.PluginCommonProxy;
import com.countrygamer.countrygamercore.Base.common.network.MessageSyncExtendedProperties;
import com.countrygamer.countrygamercore.Base.common.network.PacketHandler;

public class CommonProxy implements PluginCommonProxy {
	
	@Override
	public void registerRender() {
	}
	
	public void syncPacket(MessageSyncExtendedProperties message, EntityPlayer player) {
		PacketHandler.sendToPlayer(Core.pluginID, message, player);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
	
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
	
}
