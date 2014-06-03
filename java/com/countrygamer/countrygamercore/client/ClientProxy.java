package com.countrygamer.countrygamercore.client;

import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.countrygamercore.Base.common.network.MessageSyncExtendedProperties;
import com.countrygamer.countrygamercore.Base.common.network.PacketHandler;
import com.countrygamer.countrygamercore.common.CommonProxy;
import com.countrygamer.countrygamercore.common.Core;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void syncPacket(MessageSyncExtendedProperties message, EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			PacketHandler.sendToServer(Core.pluginID, message);
		}
		else {
			super.syncPacket(message, player);
		}
	}
	
}
