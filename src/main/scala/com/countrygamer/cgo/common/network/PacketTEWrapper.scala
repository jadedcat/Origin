package com.countrygamer.cgo.common.network

import com.countrygamer.cgo.wrapper.common.network.AbstractPacket
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author CountryGamer
 */
class PacketTEWrapper(var x: Int, var y: Int, var z: Int) extends AbstractPacket {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(0, 0, 0)
	}
	// End Constructors

	override def writeTo(buffer: ByteBuf): Unit = {
		buffer.writeInt(this.x)
		buffer.writeInt(this.y)
		buffer.writeInt(this.z)

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		this.x = buffer.readInt()
		this.y = buffer.readInt()
		this.z = buffer.readInt()

	}

	override def handleOnClient(player: EntityPlayer): Unit = {
		this.handleSync(player, player.worldObj.getTileEntity(this.x, this.y, this.z))

	}

	override def handleOnServer(player: EntityPlayer): Unit = {
		this.handleSync(player, player.worldObj.getTileEntity(this.x, this.y, this.z))

	}

	def handleSync(player: EntityPlayer, tileEntity: TileEntity): Unit = {

	}

}
