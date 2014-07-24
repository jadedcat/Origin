package com.countrygamer.cgo.wrapper.common.registries

import com.countrygamer.cgo.common.network.PacketHandler
import com.countrygamer.cgo.wrapper.common.extended.{ExtendedEntity, ExtendedEntityHandler}
import com.countrygamer.cgo.wrapper.common.network.AbstractPacket
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, IFuelHandler}
import net.minecraftforge.common.MinecraftForge

/**
 *
 *
 * @author CountryGamer
 */
object RegisterHelper {

	def registerHandler(eventHandler: Object, fuelHandler: IFuelHandler): Unit = {
		if (eventHandler != null) {
			MinecraftForge.EVENT_BUS.register(eventHandler)
			FMLCommonHandler.instance().bus().register(eventHandler)
		}
		if (fuelHandler != null) {
			GameRegistry.registerFuelHandler(fuelHandler)
		}
	}

	def registerPacketHandler(pluginID: String, messages: Class[_ <: AbstractPacket]*) {
		if (FMLCommonHandler.instance().getEffectiveSide.isClient) {
			PacketHandler.registerHandler(pluginID, messages.toArray)
		}
	}

	def registerExtendedPlayer(classKey: String, extendedClass: Class[_ <: ExtendedEntity],
			deathPersistance: Boolean): Unit = {
		ExtendedEntityHandler.registerExtended(classKey, extendedClass, deathPersistance)
	}

}
