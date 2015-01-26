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

	def initRendering(modid: String, name: String): Unit =
		if (WorldHelper.isClient()) this.registerRendering(modid, name)

	def getItem(): Item = null

	@SideOnly(Side.CLIENT)
	private def registerRendering(modid: String, name: String): Unit = {
		if (this.hasCustomItemMesh())
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
				this.getItem(), this.getItemMesh()
			)
		else
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(
				this.getItem(), 0, this.getModelLocation(modid, name)
			)
	}

	@SideOnly(Side.CLIENT)
	def hasCustomItemMesh(): Boolean = false

	@SideOnly(Side.CLIENT)
	def getItemMesh(): ItemMeshDefinition = {
		new ItemMeshDefinition {
			override def getModelLocation(itemStack: ItemStack): ModelResourceLocation = {
				//todo new ModelResourceLocation()
				null
			}
		}
	}

	@SideOnly(Side.CLIENT)
	def getModelLocation(modid: String, name: String): ModelResourceLocation = {
		new ModelResourceLocation(modid + ":" + name, "inventory")
	}



}
