package com.temportalist.origin.wrapper.client.render.model

import java.util

import com.temportalist.origin.library.client.render.model.IModel
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.Entity

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class ModelWrapper(texWidth: Int, texHeight: Int) extends ModelBase with IModel {

	val modelList: util.ArrayList[ModelRenderer] = new util.ArrayList[ModelRenderer]()

	// Default Constructor
	{
		this.textureWidth = texWidth
		this.textureHeight = texHeight

	}

	// End Constructor

	override def addModel(model: ModelRenderer): Unit = {
		this.modelList.add(model)
	}

	/**
	 *
	 * @param model The model
	 * @param x Rotation around x axis in radians
	 * @param y Rotation around y axis in radians
	 * @param z Rotation around z axis in radians
	 */
	override def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float): Unit = {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z

	}

	override def render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float,
			f5: Float): Unit = {
		super.render(entity, f, f1, f2, f3, f4, f5)
		this.renderModel(f5)

	}

	def renderModel(f5: Float): Unit = {
		for (i <- 0 until this.modelList.size()) {
			this.modelList.get(i).render(f5)
		}
	}

	@Deprecated
	override def setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float,
			entity: Entity): Unit = {
	}

}

@SideOnly(Side.CLIENT)
object ModelWrapper {

	val f5: Float = 0.0625F

}
