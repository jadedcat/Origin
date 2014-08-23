package com.countrygamer.cgo.client.render.model

import net.minecraft.client.model.ModelRenderer

/**
 *
 *
 * @author CountryGamer
 */
trait IModel {

	def addModel(model: ModelRenderer)

	def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float)

}
