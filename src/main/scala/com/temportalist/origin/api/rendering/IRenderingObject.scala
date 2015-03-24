package com.temportalist.origin.api.rendering

import com.temportalist.origin.library.client.utility.Rendering
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.client.resources.model.{IBakedModel, ModelResourceLocation}
import net.minecraft.item.{Item, ItemStack}
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
		Rendering.itemMesher.register(
			this.getItem(),
			new ItemMeshDefinition {
				override def getModelLocation(stack: ItemStack): ModelResourceLocation =
					getModelLoc(isItem = true, stack)
			}
		)
		if (this.getBlock() != null) ModelLoader.setCustomStateMapper(
			this.getBlock(), new StateMapperBase {
				override def getModelResourceLocation(
						iBlockState: IBlockState): ModelResourceLocation =
					getModelLoc(isItem = false, null)
			}
		)
	}

	@SideOnly(Side.CLIENT)
	def bakeModel(reg: IRegistry): Unit = {
		if (this.getBakedModel() != null) {
			reg.getObject(this.getModelLoc(this.isInstanceOf[Item], null), this.getBakedModel())
		}
	}

	@SideOnly(Side.CLIENT)
	def getModelLoc(isItem: Boolean, stack: ItemStack): ModelResourceLocation = {
		new ModelResourceLocation(this.getCompoundName(),
			if (isItem) "inventory" else "normal"
		)
	}

	@SideOnly(Side.CLIENT)
	def getBakedModel(): IBakedModel = null

}
