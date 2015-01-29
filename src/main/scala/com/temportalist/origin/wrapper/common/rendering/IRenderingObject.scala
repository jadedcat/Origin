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

	def getItem(): Item

	def getName(): String

	@SideOnly(Side.CLIENT)
	private def registerRendering(modid: String, name: String): Unit = {
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
		new ModelResourceLocation(this.getName(), null)
	}



}
