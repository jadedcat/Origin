package com.temportalist.origin.library.client.render.model_old

import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.ModelRenderer

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
trait IModel {

	def addModel(model: ModelRenderer)

	def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float)

}
