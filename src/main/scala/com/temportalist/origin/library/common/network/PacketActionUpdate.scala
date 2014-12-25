package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.lib.enums.ActivatedAction
import com.temportalist.origin.library.common.lib.vec.BlockCoord
import com.temportalist.origin.wrapper.common.network.PacketTEWrapper
import com.temportalist.origin.wrapper.common.tile.IAction
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos

/**
 *
 *
 * @author TheTemportalist
 */
class PacketActionUpdate(pos: BlockPos, var state: ActivatedAction) extends PacketTEWrapper(pos) {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(BlockPos.ORIGIN, null)
	}

	// End Constructors

	override def writeTo(buffer: ByteBuf): Unit = {
		super.writeTo(buffer)
		buffer.writeInt(ActivatedAction.getInt(this.state))

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		super.readFrom(buffer)
		this.state = ActivatedAction.getState(buffer.readInt())

	}

	override def handleSync(player: EntityPlayer, tileEntity: TileEntity): Unit = {
		if (tileEntity != null && tileEntity.isInstanceOf[IAction]) {
			tileEntity.asInstanceOf[IAction].setAction(this.state)

			new BlockCoord(tileEntity).notifyAllOfStateChange()

		}
	}

}
