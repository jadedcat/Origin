package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.nethandler.IPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos

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
		val tile: TileEntity = player.worldObj.getTileEntity(this.get[BlockPos])
		if (tile != null) this.handle(player, tile, isServer)
	}

	abstract def handle(player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit

}
