package com.countrygamer.countrygamercore.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ResourceHelper {
	
	public static ResourceLocation getResource(String pluginID, String folder, String imgName) {
		return new ResourceLocation(pluginID, "textures/" + folder + imgName + ".png");
	}
	
	@SideOnly(Side.CLIENT)
	public static void bindResource(ResourceLocation rl) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(rl);
	}
	
}
