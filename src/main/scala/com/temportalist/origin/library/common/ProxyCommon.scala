package com.temportalist.origin.library.common

import com.temportalist.origin.api.IProxy
import com.temportalist.origin.api.rendering.ISpriteMapper
import com.temportalist.origin.library.common.nethandler.PacketHandler
import com.temportalist.origin.library.common.network.PacketSyncExtendedProperties
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class ProxyCommon extends IProxy {

	override def registerRender(): Unit = {}

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
		PacketHandler.sendToPlayer(Origin.MODID, message, player)
	}

	def addArmor(armor: String): Int = {
		0
	}

}
