package com.temportalist.origin.api.tile

import com.temportalist.origin.library.common.network.PacketTileCallback
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
trait IPacketCallback extends TileEntity {

	def packetCallback(packet: PacketTileCallback, isServer: Boolean): Unit

}
