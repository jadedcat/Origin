package com.temportalist.origin.wrapper.common.rendering

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.client.resources.model.{IBakedModel, ModelResourceLocation}
import net.minecraft.item.Item
import net.minecraft.util.IRegistry
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
trait IRenderingObject {

	def getItem(): Item

	def getBlock(): Block = null

	def getCompoundName(): String

	@SideOnly(Side.CLIENT)
	def registerRendering(): Unit = {
		/*
		Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
			this.getItem(), this.getItemMesh()
		)
		*/
		ModelLoader.setCustomModelResourceLocation(
			this.getItem(), 0, this.getModelLoc()
		)
		if (this.getBlock() != null) ModelLoader.setCustomStateMapper(
			this.getBlock(), new StateMapperBase {
				override def getModelResourceLocation(
						iBlockState: IBlockState): ModelResourceLocation = getModelLoc()
			}
		)
	}

	@SideOnly(Side.CLIENT)
	def bakeModel(reg: IRegistry): Unit = {
		if (this.getBakedModel() != null) {
			reg.getObject(this.getBakedModel())
		}
	}

	@SideOnly(Side.CLIENT)
	def getModelLoc(): ModelResourceLocation = {
		new ModelResourceLocation(this.getCompoundName(),
			if (this.isInstanceOf[Item]) "inventory" else "normal"
		)
	}

	@SideOnly(Side.CLIENT)
	def getBakedModel(): IBakedModel = null

}
