package com.temportalist.origin.api.tile

/**
 *
 *
 * @author TheTemportalist
 */
trait IPacketCallback {

	def packetCallback(packet: PacketTileCallback, isServer: Boolean): Unit

}
