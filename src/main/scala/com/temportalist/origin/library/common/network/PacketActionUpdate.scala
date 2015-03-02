package com.temportalist.origin.library.common.network

import com.temportalist.origin.api.tile.IAction
import com.temportalist.origin.library.common.lib.enums.ActivatedAction
import com.temportalist.origin.library.common.lib.vec.BlockCoord
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
class PacketActionUpdate(tile: TileEntity, state: ActivatedAction) extends PacketTile(tile) {

	if (state != null)
		this.add(state.ordinal())

	def this() {
		this(null, null)
	}

	override def handle(
			player: EntityPlayer, tileEntity: TileEntity, isServer: Boolean): Unit = {
		if (tileEntity != null && tileEntity.isInstanceOf[IAction]) {
			tileEntity.asInstanceOf[IAction].setAction(ActivatedAction.values()(this.get[Int]))
			new BlockCoord(tileEntity).notifyAllOfStateChange()
		}
	}

}
