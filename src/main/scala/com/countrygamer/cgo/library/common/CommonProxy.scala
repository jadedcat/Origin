package com.countrygamer.cgo.library.common

import com.countrygamer.cgo.library.common.nethandler.PacketHandler
import com.countrygamer.cgo.library.common.network.PacketSyncExtendedProperties
import com.countrygamer.cgo.wrapper.common.ProxyWrapper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
class CommonProxy extends ProxyWrapper {

	override def registerRender(): Unit = {}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, coord: Vec3,
			tileEntity: TileEntity): AnyRef = {
		null
	}

	override def getServerElement(ID: Int, player: EntityPlayer, world: World, coord: Vec3,
			tileEntity: TileEntity): AnyRef = {
		null
	}

	def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		PacketHandler.sendToPlayer(Origin.pluginID, message, player)
	}

	def addArmor(armor: String): Int = {
		0
	}

}
