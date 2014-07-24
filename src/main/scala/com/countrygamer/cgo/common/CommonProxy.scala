package com.countrygamer.cgo.common

import com.countrygamer.cgo.common.network.{PacketHandler, PacketSyncExtendedProperties}
import com.countrygamer.cgo.wrapper.common.ProxyWrapper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
class CommonProxy extends ProxyWrapper {

	override def registerRender(): Unit = {}

	override def getServerGuiElement(p1: Int, p2: EntityPlayer, p3: World, p4: Int, p5: Int,
			p6: Int): AnyRef = {
		null
	}

	override def getClientGuiElement(p1: Int, p2: EntityPlayer, p3: World, p4: Int, p5: Int,
			p6: Int): AnyRef = {
		null
	}

	def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		PacketHandler.sendToPlayer(Origin.pluginID, message, player)
	}

	def addArmor(armor: String): Int = {
		0
	}

}
