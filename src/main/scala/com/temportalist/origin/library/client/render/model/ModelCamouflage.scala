package com.temportalist.origin.library.client.render.model

import java.util
import java.util.Collections

import com.temportalist.origin.library.common.lib.vec.Vector3O
import com.temportalist.origin.wrapper.common.tile.ICamouflage
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemCameraTransforms}
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.item.ItemStack
import net.minecraft.util.{BlockPos, EnumFacing}
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

	private def getCamo(world: World, state: IBlockState): ICamouflage = {
		state match {
			case extState: IExtendedBlockState =>
				val pos: BlockPos = extState.getValue(ICamouflage.CAMO_PROP)
				new Vector3O(pos).getTile(world) match {
					case camo: ICamouflage =>
						return camo
					case _ =>
				}
			case _ =>
		}
		null
	}

	override def handleBlockState(state: IBlockState): IBakedModel = {
		val worlda: World = Minecraft.getMinecraft.theWorld
		new ModelCamouflage(worlda, this.getCamo(worlda, state))
	}

	override def handleItemState(stack: ItemStack): IBakedModel = {
		val worlda: World = Minecraft.getMinecraft.theWorld
		//new ModelCamouflage(worlda, this.getCamo(worlda, state))
		null // todo
	}

	override def isBuiltInRenderer: Boolean = false

	override def getItemCameraTransforms: ItemCameraTransforms = ItemCameraTransforms.field_178357_a

	// getGeneralQuads
	override def func_177550_a(): util.List[_] = {
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

	override def isAmbientOcclusionEnabled: Boolean = true

	override def isGui3d: Boolean = true

	// getFaceQuads
	override def func_177551_a(facing: EnumFacing): util.List[_] = Collections.emptyList()
			.asInstanceOf[util.List[BakedQuad]]

}
