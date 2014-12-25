package com.temportalist.origin.library.common.lib.vec

import com.google.common.base.Objects
import com.temportalist.origin.library.common.utility.WorldHelper
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

/**
 *
 *
 * @author TheTemportalist
 */
class BlockCoord(_x: Int, _y: Int, _z: Int, var dim: Int) extends BlockPos(_x, _y, _z) {

	def this(vec: Vector3O, dim: Int) {
		this(vec.x_i(), vec.y_i(), vec.z_i(), dim)
	}

	def this(pos: BlockPos, dim: Int) {
		this(pos.getX, pos.getY, pos.getZ, dim)
	}

	def this(tile: TileEntity) {
		this(tile.getPos, tile.getWorld.provider.getDimensionId)
	}

	override def toString: String = {
		Objects.toStringHelper(this)
				.add("x", this.getX).add("y", this.getY).add("z", this.getZ).add("dim", this.dim).toString
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case blockPos: BlockCoord =>
				super.equals(blockPos) && this.dim == blockPos.dim
			case _ => false
		}
	}

	override def hashCode(): Int = {
		super.hashCode() * 31 + this.dim
	}

	def getWorld(): World = {
		WorldHelper.getWorld(this.dim)
	}

	def getBlockState(): IBlockState = this.getWorld().getBlockState(this)

	def getBlock(): Block = this.getBlockState().getBlock

	def getTile(): TileEntity = this.getWorld().getTileEntity(this)

	def getChunk(): Chunk = {
		val world: World = this.getWorld()
		if (world.isBlockLoaded(this)) {
			world.getChunkFromBlockCoords(this)
		}
		else null
	}

	def setBlock(blockState: IBlockState, notify: Int): Unit = {
		this.getWorld().setBlockState(this, blockState, notify)
	}

	def setBlock(block: Block, meta: Int, notify: Int): Unit = {
		this.setBlock(block.getStateFromMeta(meta), notify)
	}

	def toCoord(x: Int, y: Int, z: Int): BlockCoord =
		new BlockCoord(this.getX, this.getY, this.getZ, this.dim)

	def notifyAllOfStateChange(): Unit = {
		this.getWorld().notifyNeighborsOfStateChange(this, this.getBlock())
	}

	def notifyStateChange(): Unit = {
		this.getWorld().notifyBlockOfStateChange(this, this.getBlock())
	}

	def scheduleUpdate(delay: Int): Unit = {
		this.getWorld().scheduleUpdate(this, this.getBlock(), delay)
	}

	def scheduleUpdate(): Unit = this.scheduleUpdate(10)

	def markForUpdate(): Unit = this.getWorld().markBlockForUpdate(this)

}
