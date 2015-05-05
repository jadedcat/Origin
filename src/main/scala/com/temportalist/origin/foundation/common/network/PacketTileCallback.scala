package com.temportalist.origin.foundation.common.network

import com.temportalist.origin.foundation.common.tile.IPacketCallback
import cpw.mods.fml.relauncher.Side
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

	override def handle(player: EntityPlayer, tileEntity: TileEntity, side: Side): Unit = {
		tileEntity match {
			case callback: IPacketCallback =>
				callback.packetCallback(this, side)
			case _ =>
		}
	}

}
