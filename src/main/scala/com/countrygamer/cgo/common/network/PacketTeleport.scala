package com.countrygamer.cgo.common.network

import com.countrygamer.cgo.common.lib.util.UtilVector
import com.countrygamer.cgo.wrapper.common.network.AbstractPacket
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author CountryGamer
 */
class PacketTeleport(var dimId: Int, var coords: Array[Double], var fall: Boolean,
		var particles: Boolean) extends AbstractPacket {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(0, null, false, false)
	}

	// End Constructors
	override def writeTo(buffer: ByteBuf): Unit = {
		buffer.writeInt(this.dimId)
		buffer.writeDouble(coords(0))
		buffer.writeDouble(coords(1))
		buffer.writeDouble(coords(2))
		buffer.writeBoolean(this.fall)
		buffer.writeBoolean(this.particles)

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		this.dimId = buffer.readInt()
		this.coords(0) = buffer.readDouble()
		this.coords(1) = buffer.readDouble()
		this.coords(2) = buffer.readDouble()
		this.fall = buffer.readBoolean()
		this.particles = buffer.readBoolean()

	}

	override def handleOnClient(player: EntityPlayer): Unit = {}

	override def handleOnServer(player: EntityPlayer): Unit = {
		UtilVector.teleportPlayerToDimension(player, this.dimId)
		UtilVector.teleportPlayer(player, coords(0), coords(1), coords(2), fall, particles)

	}

}
