package com.temportalist.origin.library.common.lib.vec

import codechicken.lib.math.MathHelper
import codechicken.lib.vec.{BlockCoord, Vector3}
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
class BlockPos(_x: Int, _y: Int, _z: Int, var dim: Int) extends BlockCoord(_x, _y, _z) {

	def this(vec: Vector3, dim: Int) {
		this(
			MathHelper.floor_double(vec.x),
			MathHelper.floor_double(vec.y),
			MathHelper.floor_double(vec.z), dim
		)
	}

	def this(tile: TileEntity) {
		this(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj.provider.dimensionId)
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case blockPos: BlockPos =>
				super.equals(blockPos) && this.dim == blockPos.dim
			case _ => false
		}
	}

}
