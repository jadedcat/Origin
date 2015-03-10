package com.temportalist.origin.api

import net.minecraft.util.ResourceLocation

/**
 *
 *
 * @author TheTemportalist
 */
trait IResourceHandler {

	protected def getModid(): String

	def getResource(category: String, name: String): ResourceLocation = {
		new ResourceLocation(this.getModid(), this.getPath(category) + name)
	}

	private def getPath(category: String): String = {
		category match {
			case "blockstates" => "blockstates/"
			case "lang" => "lang/"
			case "modelBlock" => "models/block/"
			case "modelItem" => "models/item/"
			case "sounds" => "sounds/"
			case "texBlock" => "textures/blocks/"
			case "texItem" => "textures/items/"
			case "texModel" => "textures/models/"
			case "gui" => "textures/gui/"
			case "tex" => "textures/"
			case _ =>
				throw new IllegalArgumentException(category)
				null
		}
	}

}
