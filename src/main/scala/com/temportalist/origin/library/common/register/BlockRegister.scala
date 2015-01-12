package com.temportalist.origin.library.common.register

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 *
 *
 * @author TheTemportalist
 */
trait BlockRegister extends Register {

	/**
	 * This method is used to register TileEntities.
	 * Recommendation: Use GameRegistry.registerTileEntity
	 */
	def registerTileEntities(): Unit = {}

	def register(id: String, clazz: Class[_ <: TileEntity]): Unit =
		GameRegistry.registerTileEntity(clazz, id)

	/**
	 * This method is used to register crafting recipes
	 */
	def registerCrafting(): Unit = {}

	def registerSmelting(): Unit = {}

	def registerOther(): Unit = {}

}
