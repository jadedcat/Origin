package com.temportalist.origin.wrapper.common.rendering

import com.temportalist.origin.library.common.utility.WorldHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
trait IRenderingObject {

	def initRendering(): Unit =
		if (WorldHelper.isClient()) this.registerRendering()

	def getItem(): Item

	def getCompoundName(): String

	@SideOnly(Side.CLIENT)
	private def registerRendering(): Unit = {
		Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
			this.getItem(), this.getItemMesh()
		)
	}

	@SideOnly(Side.CLIENT)
	def getItemMesh(): ItemMeshDefinition = {
		new ItemMeshDefinition {
			override def getModelLocation(itemStack: ItemStack): ModelResourceLocation = {
				getModelLoc()
			}
		}
	}

	@SideOnly(Side.CLIENT)
	def getModelLoc(): ModelResourceLocation = {
		new ModelResourceLocation(this.getCompoundName(), "inventory")
	}



}
