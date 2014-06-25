package com.countrygamer.countrygamercore.client;

import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.countrygamercore.common.CommonProxy;
import com.countrygamer.countrygamercore.common.Core;
import com.countrygamer.countrygamercore.common.network.MessageSyncExtendedProperties;
import com.countrygamer.countrygamercore.common.network.PacketHandler;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRender() {
		RenderingRegistry.registerBlockHandler(new BlockCamouflageRender());
		
	}
	
	@Override
	public void syncPacket(MessageSyncExtendedProperties message, EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			PacketHandler.sendToServer(Core.pluginID, message);
		}
		else {
			super.syncPacket(message, player);
		}
	}
	
	@Override
	public int addArmor(String armor) {
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}
	
}
