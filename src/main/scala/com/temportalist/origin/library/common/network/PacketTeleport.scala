package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.utility.Teleport
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}

/**
 *
 *
 * @author TheTemportalist
 */
class PacketTeleport() extends IPacketOrigin {

	def this(dimId: Int, coords: Array[Double], fall: Boolean, particles: Boolean) {
		this()
		this.add(dimId)
		this.add(coords)
		this.add(fall)
		this.add(particles)
	}

	override def handle(player: EntityPlayer, isServer: Boolean): Unit = {
		if (isServer) {
			Teleport.toDimension(player, this.get[Int])
			val coord: Array[Double] = this.get[Array[Double]]
			val fall: Boolean = this.get[Boolean]
			val particles: Boolean = this.get[Boolean]
			Teleport.toPoint(player.asInstanceOf[EntityPlayerMP], coord(0), coord(1), coord(2))
		}
	}

}
