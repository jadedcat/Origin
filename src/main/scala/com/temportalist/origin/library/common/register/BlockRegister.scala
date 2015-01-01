package com.temportalist.origin.library.common.register

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

	/**
	 * This method is used to register crafting recipes
	 */
	def registerCrafting(): Unit = {}

	def registerSmelting(): Unit = {}

	def registerOther(): Unit = {}

}
