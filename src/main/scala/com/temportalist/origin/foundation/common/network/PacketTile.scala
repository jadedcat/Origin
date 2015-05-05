package com.temportalist.origin.foundation.common.network

import com.temportalist.origin.api.common.lib.V3O
import cpw.mods.fml.relauncher.Side
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

	override def handle(player: EntityPlayer, side: Side): Unit = {
		val vec: V3O = this.get[V3O]
		val tile: TileEntity = vec.getTile(player.getEntityWorld)
		if (tile != null) this.handle(player, tile, side)
	}

	def handle(player: EntityPlayer, tileEntity: TileEntity, side: Side): Unit

}
