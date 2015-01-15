package com.temportalist.origin.library.client.render.model

import java.util
import java.util.Collections

import com.temportalist.origin.library.common.utility.States
import com.temportalist.origin.wrapper.common.tile.ICamouflage
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemCameraTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.minecraftforge.client.model.{ISmartBlockModel, ISmartItemModel}
import net.minecraftforge.common.property.IExtendedBlockState
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class ModelCamouflage(world: World, camouflage: ICamouflage)
		extends ISmartBlockModel with ISmartItemModel {

	def getBlockModelForState(state: IBlockState): IBakedModel = Minecraft.getMinecraft.
			getBlockRendererDispatcher.getBlockModelShapes.getModelForState(state)

	def getModelFromBlockName(camoName: String): IBakedModel = {
		val state: IBlockState = States.getStateFromName(camoName)
		if (state != null) {
			this.getBlockModelForState(state)
		}
		else null
	}

	override def handleBlockState(state: IBlockState): IBakedModel = {
		state match {
			case ext: IExtendedBlockState =>
				return this.getModelFromBlockName(ext.getValue(ICamouflage.CAMO_PROP))
			case _ =>
		}
		this.getBlockModelForState(state)
	}

	override def handleItemState(stack: ItemStack): IBakedModel = {
		if (stack.hasTagCompound) {
			return this.getModelFromBlockName(ICamouflage.getCamoString(stack))
		}
		Minecraft.getMinecraft.getRenderItem.getItemModelMesher.getItemModel(stack)
	}

	override def isBuiltInRenderer: Boolean = false

	override def getItemCameraTransforms: ItemCameraTransforms = ItemCameraTransforms.DEFAULT

	override def getGeneralQuads(): util.List[_] = {
		val verticies: util.ArrayList[BakedQuad] = new util.ArrayList[BakedQuad]()

		for (sideIndex <- 0 until EnumFacing.values().length) {
			/* todo
			verticies.add(new BakedQuad(
				Ints.concat(
					//Rendering.modelCoordsToVerticies()
				), -1, EnumFacing.getFront(sideIndex)
			))
			*/
		}

		verticies
	}

	override def getTexture: TextureAtlasSprite = {
		if (this.camouflage.hasCamouflage) {

		}
		else {

		}
		null // todo
	}

	override def isAmbientOcclusion: Boolean = true

	override def isGui3d: Boolean = true

	override def getFaceQuads(facing: EnumFacing): util.List[_] =
		Collections.emptyList().asInstanceOf[util.List[BakedQuad]]

}
