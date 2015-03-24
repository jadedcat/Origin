package com.temportalist.origin.test.client

import java.util

import com.temportalist.origin.library.client.utility.Rendering
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemCameraTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.util.EnumFacing

/**
 *
 *
 * @author TheTemportalist
 */
trait ISmartModel extends IBakedModel {

	def getTextureLocation(): String

	override def isBuiltInRenderer: Boolean = false

	override def getItemCameraTransforms: ItemCameraTransforms = ItemCameraTransforms.DEFAULT

	override def getTexture: TextureAtlasSprite =
		Rendering.mc.getTextureMapBlocks.getAtlasSprite(this.getTextureLocation)

	override def isAmbientOcclusion: Boolean = true

	override def getGeneralQuads: util.List[_] = new util.ArrayList[BakedQuad]()

	override def isGui3d: Boolean = true

	override def getFaceQuads(facing : EnumFacing): util.List[_] = new util.ArrayList[BakedQuad]()

}
