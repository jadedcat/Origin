package com.temportalist.origin.api.rendering

import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.util.ResourceLocation

/**
 *
 *
 * @author TheTemportalist
 */
trait ISpriteMapper {

	@SideOnly(Side.CLIENT)
	def getResourceLocation(): ResourceLocation

}
