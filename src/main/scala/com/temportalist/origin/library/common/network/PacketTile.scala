package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.lib.vec.V3O
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
		//println("handling on " + isServer)
		val vec: V3O = this.get[V3O]
		//println(vec.x_i() + " | " + vec.y_i() + " | " + vec.z_i())
		val tile: TileEntity = vec.getTile(player.getEntityWorld)
		//println("PacketTile: tile == null -> " + (tile == null))
		if (tile != null) {
			this.handle(player, tile, isServer)
		}
	}

	def handle(player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit

}
