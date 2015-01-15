package com.temportalist.origin.library.server.command

import java.util

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.utility.{Player, Teleport}
import net.minecraft.command.{CommandBase, ICommandSender}
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
object TeleportCommand extends CommandBase {

	var aliases: util.List[String] = new util.ArrayList[String]()
	this.aliases.add("origin")

	override def getName: String = {
		this.aliases.get(0)
	}

	override def getAliases: util.List[_] = {
		this.aliases
	}

	override def getUsage(p1: ICommandSender): String = {
		"origin tp < [x] [y] [z] <Dimension Name:_> >"
	}

	override def execute(sender: ICommandSender, args: Array[String]): Unit = {
		if (args.length == 0) {
			return
		}

		val commandType: String = args(0)

		if (commandType.equals("tp")) {
			// origin tp ...
			sender match {
				case player1: EntityPlayer =>
					var player: EntityPlayer = player1

					if (args.length == 1) {
						Teleport.toCursorPosition(player, 20000.0D)
						return
					}

					// origin tp x      y z
					// origin tp x      y z dimName
					// origin tp player x y z
					// origin tp player x y z       dimName

					if (args.length < 4) {
						Player.message(player, "Not enough arguments")
						return
					}

					var hasAlternatePlayer: Boolean = false
					try {
						val potentialXCoord: Double = args(1).toDouble
					}
					catch {
						case e: NumberFormatException =>
							hasAlternatePlayer = true
					}

					var coordStartIndex: Int = 1
					if (hasAlternatePlayer) {
						coordStartIndex += 1
						player = Player.getPlayer(args(1))
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
						case e: NumberFormatException =>
							Player.message(player1,
								"Something went terribly wrong...")
							return
					}

					if ((!hasAlternatePlayer && args.length > 4) ||
							(hasAlternatePlayer && args.length > 5)) {
						var dimName: String = ""
						for (i <- coordStartIndex + 3 until args.length) {
							dimName = (if (dimName.equals("")) "" else dimName + " ") + args(i)
						}

						var dimID: Int = 0
						try {
							dimID = dimName.toInt
						}
						catch {
							case e: Exception =>
								if (!Origin.dimensions.containsKey(dimName)) {
									Player.message(player1,
										"\"" + dimName + "\"" + " is not a valid dimension name!")
									return
								}
								else {
									dimID = Origin.dimensions.get(dimName)
								}
						}

						Teleport.toDimension(player, dimID)

					}

					Teleport.toPoint(player, x, y, z)

				case _ =>
			}
		}

	}

}
