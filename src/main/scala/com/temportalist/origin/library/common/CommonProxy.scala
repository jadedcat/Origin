package com.temportalist.origin.library.common

import com.temportalist.origin.library.common.nethandler.PacketHandler
import com.temportalist.origin.library.common.network.PacketSyncExtendedProperties
import com.temportalist.origin.wrapper.common.ProxyWrapper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class CommonProxy extends ProxyWrapper {

	override def registerRender(): Unit = {}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		null
	}

	override def getServerElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		null
	}

	def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		PacketHandler.sendToPlayer(Origin.pluginID, message, player)
	}

	def addArmor(armor: String): Int = {
		0
	}

}