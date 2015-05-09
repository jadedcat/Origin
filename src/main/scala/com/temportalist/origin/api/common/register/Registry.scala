package com.temportalist.origin.api.common.register

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, IFuelHandler}
import net.minecraftforge.common.MinecraftForge

/**
 *
 *
 * @author  TheTemportalist  5/7/15
 */
object Registry {

	def registerHandler(handlers: Object*): Unit = {
		for (o: Object <- handlers) if (o != null) {
			MinecraftForge.EVENT_BUS.register(o)
			FMLCommonHandler.instance().bus().register(o)
		}
	}

	def registerFuelHandler(fuelHandlers: IFuelHandler*): Unit = {
		for (o: IFuelHandler <- fuelHandlers) if (o != null) {
			GameRegistry.registerFuelHandler(o)
		}
	}

}
