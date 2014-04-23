package com.countrygamer.core.client;


import com.countrygamer.core.client.render.TileEntityDiagramRenderer;
import com.countrygamer.core.common.ServerProxy;
import com.countrygamer.core.common.tileentity.TileEntityDiagram;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends ServerProxy {

	public void preInit() {
	}

	public void registerRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiagram.class, new TileEntityDiagramRenderer());
	}

	public void registerHandler() {

	}

}
