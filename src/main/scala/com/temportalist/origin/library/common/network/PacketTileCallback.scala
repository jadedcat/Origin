package com.temportalist.origin.library.common.network

import com.temportalist.origin.api.tile.IPacketCallback
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
class PacketTileCallback(tile: TileEntity) extends PacketTile(tile) {

	def this() {
		this(null)
	}

	override def handle(player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit = {
		tileEntity match {
			case callback: IPacketCallback =>
				callback.packetCallback(this, isServer)
			case _ =>
		}
	}

}
