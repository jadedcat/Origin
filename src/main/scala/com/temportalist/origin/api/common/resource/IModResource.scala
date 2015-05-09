package com.temportalist.origin.api.common.resource

import net.minecraft.util.ResourceLocation

/**
 *
 *
 * @author TheTemportalist
 */
trait IModResource extends IModDetails {

	def getResource(resourceType: EnumResource, name: String): ResourceLocation = {
		new ResourceLocation(this.getModid, resourceType.getPath + "/" + name)
	}

}
