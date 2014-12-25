package com.temportalist.origin.wrapper.common

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

	def initRendering(): Unit = if (WorldHelper.isClient()) this.registerRendering()

	def getItem(): Item = null

	@SideOnly(Side.CLIENT)
	private def registerRendering(): Unit = {
		if (this.hasCustomItemMesh())
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
				this.getItem(), this.getItemMesh()
			)
		else
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
				this.getItem(), 0, this.getModelLocation()
			)
	}

	@SideOnly(Side.CLIENT)
	def hasCustomItemMesh(): Boolean = false

	@SideOnly(Side.CLIENT)
	def getItemMesh(): ItemMeshDefinition = {
		new ItemMeshDefinition {
			override def getModelLocation(itemStack: ItemStack): ModelResourceLocation = {
				getModelLocation(itemStack)
			}
		}
	}

	@SideOnly(Side.CLIENT)
	def getModelLocation(): ModelResourceLocation = {
		null
	}

}
