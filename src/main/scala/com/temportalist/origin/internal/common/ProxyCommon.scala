package com.temportalist.origin.internal.common

import com.temportalist.origin.api.common.proxy.IProxy
import com.temportalist.origin.api.common.rendering.ISpriteMapper
import com.temportalist.origin.foundation.common.network.PacketSyncExtendedProperties
import com.temportalist.origin.internal.common.network.handler.Network
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class ProxyCommon extends IProxy {

	override def register(): Unit = {}

	def registerSpritee(spritee: ISpriteMapper): Unit = {}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		null
	}

	override def getServerElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		null
	}

	def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		Network.sendToPlayer( message, player)
	}

	def addArmor(armor: String): Int = {
		0
	}

}