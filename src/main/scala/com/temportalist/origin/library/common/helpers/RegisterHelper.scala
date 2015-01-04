package com.temportalist.origin.library.common.helpers

import java.util

import scala.collection.JavaConversions._

import com.temportalist.origin.library.common.nethandler.{IPacket, PacketHandler}
import com.temportalist.origin.wrapper.common.extended.{ExtendedEntity, ExtendedEntityHandler}
import net.minecraft.command.ICommand
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{FMLCommonHandler, IFuelHandler}

/**
 *
 *
 * @author TheTemportalist
 */
object RegisterHelper {

	def registerHandlers(handlers: Object*): Unit = {
		for (o: Object <- handlers)
			this.registerHandler(o)
	}

	def registerHandler(eventHandler: Object): Unit = {
		if (eventHandler != null) {
			MinecraftForge.EVENT_BUS.register(eventHandler)
			FMLCommonHandler.instance().bus().register(eventHandler)
		}
	}

	def registerFuelHandler(fuelHandler: IFuelHandler): Unit = {
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
