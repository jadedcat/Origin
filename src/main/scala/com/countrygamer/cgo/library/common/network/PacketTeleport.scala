package com.countrygamer.cgo.library.common.network

import com.countrygamer.cgo.library.common.nethandler.IPacket
import com.countrygamer.cgo.library.common.utility.Teleport
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}

/**
 *
 *
 * @author CountryGamer
 */
class PacketTeleport(var dimId: Int, var coords: Array[Double], var fall: Boolean,
		var particles: Boolean) extends IPacket {

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
		Teleport.toDimension(player, this.dimId)
		Teleport.toPoint(player.asInstanceOf[EntityPlayerMP], coords(0), coords(1), coords(2))

	}

}
