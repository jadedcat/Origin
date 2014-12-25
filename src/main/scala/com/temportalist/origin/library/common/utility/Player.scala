package com.temportalist.origin.library.common.utility

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
object Player {

	def getPlayer(senderNameORuuid: AnyRef): EntityPlayerMP = {
		val players: java.util.List[_] =
			MinecraftServer.getServer.getConfigurationManager.playerEntityList
		for (i <- 0 until players.size()) {
			players.get(i) match {
				case player: EntityPlayerMP =>
					if (player.getName.equals(senderNameORuuid) ||
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

	def getReachDistance(player: EntityPlayer): Double = {
		if (player.getEntityWorld.isRemote)
			this.getReach_client()
		else player match {
			case mp: EntityPlayerMP => this.getReach_server(mp)
			case _ => 5.0D
		}
	}

	@SideOnly(Side.CLIENT)
	private def getReach_client(): Double = Minecraft.getMinecraft.playerController.getBlockReachDistance

	private def getReach_server(mp: EntityPlayerMP): Double = mp.theItemInWorldManager.getBlockReachDistance

}
