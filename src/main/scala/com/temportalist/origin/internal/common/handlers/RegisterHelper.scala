package com.temportalist.origin.internal.common.handlers

import java.util

import com.temportalist.origin.api.common.extended.ExtendedEntity
import com.temportalist.origin.api.common.rendering.ISpriteMapper
import com.temportalist.origin.internal.common.Origin
import com.temportalist.origin.internal.common.extended.ExtendedEntityHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, IFuelHandler}
import net.minecraft.command.ICommand
import net.minecraftforge.common.MinecraftForge

import scala.collection.JavaConversions._

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
