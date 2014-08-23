package com.countrygamer.cgo.client.render.model

import net.minecraft.client.model.{ModelBase, ModelRenderer}

/**
 *
 *
 * @author CountryGamer
 */
object ModelHelper {

	def createModel(modelInstance: ModelBase, modelInstance2: IModel,
			originX: Float, originY: Float, originZ: Float,
			offsetX: Float, offsetY: Float, offsetZ: Float,
			rotationX: Float, rotationY: Float, rotationZ: Float,
			width: Int, height: Int, length: Int,
			offsetU: Int, offsetV: Int): ModelRenderer = {
		val model: ModelRenderer = new ModelRenderer(modelInstance, offsetU, offsetV)
		model.setRotationPoint(originX, 8 - originY, originZ)
		model.addBox(offsetX, offsetY, offsetZ, width, height, length)
		model.setTextureSize(modelInstance.textureWidth, modelInstance.textureHeight)
		model.mirror = true
		model.rotateAngleX = Math.toRadians(rotationX)
		model.rotateAngleY = Math.toRadians(rotationY)
		model.rotateAngleZ = Math.toRadians(rotationZ)

		modelInstance2.addModel(model)

		model
	}

}
