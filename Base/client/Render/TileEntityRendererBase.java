package com.countrygamer.core.Base.client.Render;

import net.minecraft.world.World;

/**
 * Created by Country_Gamer on 3/19/14.
 */
public interface TileEntityRendererBase {

	public void renderBasicModelForTile(
			World world, int x, int y, int z,
			double d1, double d2, double d3, float f);

}
