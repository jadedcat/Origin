package com.countrygamer.core.proxy;


import com.countrygamer.core.blocks.tile.TileEntityDiagram;
import com.countrygamer.core.client.render.TileEntityDiagramRenderer;
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
