package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.lib.vec.BlockPos
import com.temportalist.origin.library.common.nethandler.IPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
abstract class PacketTile() extends IPacket {

	def this(tile: TileEntity) {
		this()
		this.add(tile)
	}

	override def handle(player: EntityPlayer, isServer: Boolean): Unit = {
		val tile: TileEntity = this.get[BlockPos].getTile(player.getEntityWorld)
		if (tile != null) this.handle(player, tile, isServer)
	}

	def handle(player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit

}
