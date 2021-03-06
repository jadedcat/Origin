package com.temportalist.origin.foundation.common.network

import com.temportalist.origin.api.common.lib.BlockCoord
import com.temportalist.origin.api.common.tile.{ActivatedAction, IAction}
import cpw.mods.fml.relauncher.Side
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

	override def handle(player: EntityPlayer, tileEntity: TileEntity, side: Side): Unit = {
		if (tileEntity != null && tileEntity.isInstanceOf[IAction]) {
			tileEntity.asInstanceOf[IAction].setAction(ActivatedAction.values()(this.get[Int]))
			new BlockCoord(tileEntity).notifyAllOfStateChange()
		}
	}

}
