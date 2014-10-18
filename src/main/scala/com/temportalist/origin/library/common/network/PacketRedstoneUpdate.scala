package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.lib.enums.RedstoneState
import com.temportalist.origin.wrapper.common.network.PacketTEWrapper
import com.temportalist.origin.wrapper.common.tile.IPowerable
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author CountryGamer
 */
class PacketRedstoneUpdate(x: Int, y: Int, z: Int, var state: RedstoneState)
		extends PacketTEWrapper(x, y, z) {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(0, 0, 0, null)
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

			val block: Block = player.worldObj.getBlock(this.x, this.y, this.z)
			player.worldObj.notifyBlockOfNeighborChange(x + 1, y + 0, z + 0, block)
			player.worldObj.notifyBlockOfNeighborChange(x - 1, y + 0, z + 0, block)
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 1, z + 0, block)
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y - 1, z + 0, block)
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 0, z + 1, block)
			player.worldObj.notifyBlockOfNeighborChange(x + 0, y + 0, z - 1, block)

		}
	}

}
