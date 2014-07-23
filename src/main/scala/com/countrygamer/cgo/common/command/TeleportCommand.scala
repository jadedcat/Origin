package com.countrygamer.cgo.common.command

import java.util

import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.common.lib.util.{UtilVector, Player}
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author CountryGamer
 */
object TeleportCommand extends CommandBase {

	var aliases: util.List[String] = new util.ArrayList[String]()
	this.aliases.add("origin")

	override def getCommandName: String = {
		return this.aliases.get(0)
	}

	override def getCommandAliases: util.List[_] = {
		this.aliases
	}

	override def getCommandUsage(p1: ICommandSender): String = {
		return "origin [tp [x] [y] [z] <Dimension Name>:null]"
	}

	override def processCommand(sender: ICommandSender, args: Array[String]): Unit = {
		/*
		if (args.length == 0) {
			return
		}

		val playerName: String = args(0)
		val dimensionName: String = args(1)
		var x: Double = 0.0
		var y: Double = 0.0
		var z: Double = 0.0
		try {
			x = args(2).toDouble
			y = args(3).toDouble
			z = args(4).toDouble
		}
		catch {
			case e: NumberFormatException => {
				if (commandSender.isInstanceOf[EntityPlayer])
					Player.sendMessageToPlayer(commandSender.asInstanceOf[EntityPlayer],
						"Cannot parse arguments")
			}
		}
		*/
		if (args.length == 0) {
			return
		}

		val commandType: String = args(0)

		if (commandType.equals("tp")) {
			// origin tp ...
			if (sender.isInstanceOf[EntityPlayer]) {
				var player: EntityPlayer = sender.asInstanceOf[EntityPlayer]

				if (args.length == 1) {
					UtilVector.teleportVector(player.worldObj, player, 20000.0D)
					return
				}

				// origin tp x      y z
				// origin tp x      y z dimName
				// origin tp player x y z
				// origin tp player x y z       dimName

				if (args.length < 4) {
					Player.sendMessageToPlayer(player, "Not enough arguments")
					return
				}

				var hasAlternatePlayer: Boolean = false
				try {
					val potentialXCoord: Double = args(1).toDouble
				}
				catch {
					case e: NumberFormatException => {
						hasAlternatePlayer = true
					}
				}

				var coordStartIndex: Int = 1
				if (hasAlternatePlayer) {
					coordStartIndex += 1
					player = Player.getPlayerByUsername(args(1))
				}

				var x: Double = 0.0D
				var y: Double = 0.0D
				var z: Double = 0.0D
				try {
					x = args(coordStartIndex + 0).toDouble
					y = args(coordStartIndex + 1).toDouble
					z = args(coordStartIndex + 2).toDouble
				}
				catch {
					case e: NumberFormatException => {
						Player.sendMessageToPlayer(sender.asInstanceOf[EntityPlayer],
							"Something went terribly wrong...")
						return
					}
				}

				if ((!hasAlternatePlayer && args.length > 4) ||
						(hasAlternatePlayer && args.length > 5)) {
					val dimName: String = args(coordStartIndex + 3)

					if (!Origin.dimensions.containsKey(dimName)) {
						Player.sendMessageToPlayer(sender.asInstanceOf[EntityPlayer],
							"That was not a valid dimension name!")
						return
					}

					val dimID: Int = Origin.dimensions.get(dimName)

					UtilVector.teleportPlayerToDimension(player, dimID)

				}

				UtilVector.teleportPlayer(player, x, y, z, false, true)

			}
		}

	}

}
