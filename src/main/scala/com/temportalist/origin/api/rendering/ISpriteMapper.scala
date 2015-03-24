package com.temportalist.origin.api.rendering

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
trait ISpriteMapper {

	@SideOnly(Side.CLIENT)
	def getResourceLocation(): ResourceLocation

}
