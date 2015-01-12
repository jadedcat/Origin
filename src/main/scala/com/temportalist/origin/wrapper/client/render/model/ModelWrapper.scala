package com.temportalist.origin.wrapper.client.render.model

import com.temportalist.origin.library.common.lib.vec.Vector3O
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.Entity
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class ModelWrapper(texWidth: Int, texHeight: Int) extends ModelBase {

	this.textureWidth = texWidth
	this.textureHeight = texHeight

	/**
	 *
	 * @param model The model
	 * @param rot Rotation around xyz axis' in degrees
	 */
	def setRotation(model: ModelRenderer, rot: Vector3O): Unit = {
		model.rotateAngleX = Math.toRadians(rot.x).toFloat
		model.rotateAngleY = Math.toRadians(rot.y).toFloat
		model.rotateAngleZ = Math.toRadians(rot.z).toFloat
	}

	override def render(entity: Entity, parTime: Float, parSwingSuppress: Float, unknown1: Float,
			headAngleY: Float, headAngleX: Float, unknown2: Float): Unit = {
		this.renderModel(unknown2)
	}

	def renderModel(f5: Float): Unit = {}

	@Deprecated
	override def setRotationAngles(parTime: Float, parSwingSuppress: Float, unknown1: Float,
			headAngleY: Float, headAngleX: Float, unknown2: Float, entity: Entity): Unit = {}

	protected def add(parent: ModelRenderer, child: ModelRenderer): Unit = {
		parent.addChild(child)
	}

	protected def createModel(origin: Vector3O, offset: Vector3O,
			bounds: Vector3O, rot: Vector3O, u: Int, v: Int): ModelRenderer = {
		val mr: ModelRenderer = new ModelRenderer(this, u, v)
		mr.setRotationPoint(origin.x_f(), origin.y_f(), origin.z_f())
		mr.addBox(
			-offset.x_f(), -offset.y_f(), -offset.z_f(),
			bounds.x_i(), bounds.y_i(), bounds.z_i()
		)
		mr.setTextureSize(this.textureWidth, this.textureHeight)
		mr.rotateAngleX = Math.toRadians(rot.x).asInstanceOf[Float]
		mr.rotateAngleY = Math.toRadians(rot.y).asInstanceOf[Float]
		mr.rotateAngleZ = Math.toRadians(rot.z).asInstanceOf[Float]
		mr
	}

}

@SideOnly(Side.CLIENT)
object ModelWrapper {

	val f5: Float = 0.0625F

}
