package com.temportalist.origin.api.tile

import com.temportalist.origin.library.common.network.PacketTileCallback

/**
 *
 *
 * @author TheTemportalist
 */
trait IPacketCallback {

	def packetCallback(packet: PacketTileCallback, isServer: Boolean): Unit

}
