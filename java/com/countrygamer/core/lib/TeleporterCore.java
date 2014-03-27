package com.countrygamer.core.lib;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterCore extends Teleporter {

	public TeleporterCore(WorldServer worldServer) {
		super(worldServer);
	}

	public boolean makePortal(Entity par1Entity) {
		return true;
	}

	public boolean placeInExistingPortal(Entity par1Entity, double par2,
			double par4, double par6, float par8) {
		return true;
	}
	
	
}
