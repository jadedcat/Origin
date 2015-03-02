package com.temportalist.origin.library.common.network

import com.temportalist.origin.api.tile.IPowerable
import com.temportalist.origin.library.common.lib.enums.RedstoneState
import com.temportalist.origin.library.common.lib.vec.BlockCoord
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
class PacketRedstoneUpdate(tile: TileEntity, state: RedstoneState) extends PacketTile(tile) {

	if (state != null)
		this.add(state.ordinal())

	def this() {
		this(null, null)
	}

	override def handle(
			player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit = {
		if (tileEntity != null && tileEntity.isInstanceOf[IPowerable]) {
			tileEntity.asInstanceOf[IPowerable].setRedstoneState(RedstoneState.values()(this.get[Int]))
			new BlockCoord(tileEntity).notifyAllOfStateChange()
		}
	}

}
