package com.temportalist.origin.library.common.handlers

import java.util

import scala.collection.JavaConversions._

import com.temportalist.origin.api.rendering.ISpriteMapper
import com.temportalist.origin.library.common.Origin
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

	def registerHandler(handlers: Object*): Unit = {
		for (o: Object <- handlers) if (o != null) {
			MinecraftForge.EVENT_BUS.register(o)
			FMLCommonHandler.instance().bus().register(o)
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

	def registerSpritee(spritee: ISpriteMapper): Unit = {
		Origin.proxy.registerSpritee(spritee)
	}

	private val commands: util.List[ICommand] = new util.ArrayList[ICommand]()

	def registerCommand(command: ICommand): Unit = {
		this.commands.add(command)
	}

	def getCommands(): scala.List[ICommand] = {
		this.commands.toList
	}

}
