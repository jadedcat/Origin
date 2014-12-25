package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.lib.enums.RedstoneState
import com.temportalist.origin.library.common.lib.vec.BlockCoord
import com.temportalist.origin.wrapper.common.network.PacketTEWrapper
import com.temportalist.origin.wrapper.common.tile.IPowerable
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos

/**
 *
 *
 * @author TheTemportalist
 */
class PacketRedstoneUpdate(pos: BlockPos, var state: RedstoneState)
		extends PacketTEWrapper(pos) {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(BlockPos.ORIGIN, null)
	}

	// End Constructors

	override def writeTo(buffer: ByteBuf): Unit = {
		super.writeTo(buffer)
		buffer.writeInt(RedstoneState.getIntFromState(this.state))

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		super.readFrom(buffer)
		this.state = RedstoneState.getStateFromInt(buffer.readInt())

	}

	override def handleSync(player: EntityPlayer, tileEntity: TileEntity): Unit = {
		if (tileEntity != null && tileEntity.isInstanceOf[IPowerable]) {
			tileEntity.asInstanceOf[IPowerable].setRedstoneState(this.state)

			val tileCoord: BlockCoord = new BlockCoord(tileEntity)
			tileCoord.notifyAllOfStateChange()

		}
	}

}
