package com.countrygamer.cgo.library.common.helpers

import java.util

import scala.collection.JavaConversions._

import com.countrygamer.cgo.library.common.nethandler.{IPacket, PacketHandler}
import com.countrygamer.cgo.wrapper.common.extended.{ExtendedEntity, ExtendedEntityHandler}
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, IFuelHandler}
import net.minecraft.command.ICommand
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

	def registerPacketHandler(pluginID: String, messages: Class[_ <: IPacket]*) {
		if (FMLCommonHandler.instance().getEffectiveSide.isClient) {
			PacketHandler.registerHandler(pluginID, messages.toArray)
		}
	}

	def registerExtendedPlayer(classKey: String, extendedClass: Class[_ <: ExtendedEntity],
			deathPersistance: Boolean): Unit = {
		ExtendedEntityHandler.registerExtended(classKey, extendedClass, deathPersistance)
	}

	private val commands: util.List[ICommand] = new util.ArrayList[ICommand]()

	def registerCommand(command: ICommand): Unit = {
		this.commands.add(command)
	}

	def getCommands(): scala.List[ICommand] = {
		this.commands.toList
	}

}
