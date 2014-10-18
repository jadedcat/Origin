package com.temportalist.origin.library.client.render.model

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.ModelRenderer

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
trait IModel {

	def addModel(model: ModelRenderer)

	def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float)

}
