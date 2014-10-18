package com.temportalist.origin.library.common.utility

import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChatComponentText

/**
 *
 *
 * @author CountryGamer
 */
object Player {

	def getPlayer(senderNameORuuid: AnyRef): EntityPlayerMP = {
		val players: java.util.List[_] =
			MinecraftServer.getServer.getConfigurationManager.playerEntityList
		for (i <- 0 until players.size()) {
			players.get(i) match {
				case player: EntityPlayerMP =>
					if (player.getCommandSenderName.equals(senderNameORuuid) ||
							player.getUniqueID.equals(senderNameORuuid)) {
						return player
					}
				case _ =>
			}
		}
		null
	}

	def forceMoveInDirection(player: EntityPlayerMP, distance: Double,
			considerObstacles: Boolean): Unit = {
		val rotH: Double = Math.toRadians(player.rotationYaw)
		val rotV: Double = Math.toRadians(player.rotationPitch)
		val dx: Double = -Math.sin(rotH) * distance
		val dy: Double = Math.sin(rotV) * distance
		val dz: Double = Math.cos(rotH) * distance

		if (considerObstacles) {
			// todo how to determine dy correctly?
			player.moveEntity(dx, 0, dz)
		}
		else {
			Teleport.toPoint(player, player.posX + dx, player.posY + dy, player.posZ + dz)
		}
	}

	def message(player: EntityPlayer, message: String): Unit = {
		player.addChatComponentMessage(new ChatComponentText(message))
	}

}
