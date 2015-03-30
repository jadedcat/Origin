package com.temportalist.origin.library.common.lib.vec

/**
 *
 *
 * @author TheTemportalist 3/29/15
 */
class BlockPos(x: Int, y: Int, z: Int) extends V3O(x, y, z) {

	def getX(): Int = this.x_i()

	def getY(): Int = this.y_i()

	def getZ(): Int = this.z_i()

}
