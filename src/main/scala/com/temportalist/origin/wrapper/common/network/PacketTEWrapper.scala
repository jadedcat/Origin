package com.temportalist.origin.wrapper.common.network

import com.temportalist.origin.library.common.nethandler.IPacket
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos

/**
 *
 *
 * @author TheTemportalist
 */
class PacketTEWrapper(private var pos: BlockPos) extends IPacket {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(BlockPos.ORIGIN)
	}

	// End Constructors

	override def writeTo(buffer: ByteBuf): Unit = {
		buffer.writeInt(this.pos.getX)
		buffer.writeInt(this.pos.getY)
		buffer.writeInt(this.pos.getZ)

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		this.pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt())
	}

	override def handle(player: EntityPlayer): Unit = {
		this.handleSync(player, player.worldObj.getTileEntity(this.pos))
	}

	def handleSync(player: EntityPlayer, tileEntity: TileEntity): Unit = {

	}

}
