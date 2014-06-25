package com.countrygamer.countrygamercore.base.common.tile;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;

public class FakePlayer extends EntityPlayerMP {
	
	public FakePlayer(WorldServer world) {
		super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, new GameProfile(
				UUID.fromString(""), "[CGCore]"), new ItemInWorldManager(world));
		// new NetServerHandlerFake(FMLCommonHandler.instance().getMinecraftServerInstance(), this);
	}
	
}
