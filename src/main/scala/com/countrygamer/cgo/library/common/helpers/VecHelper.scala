package com.countrygamer.cgo.library.common.helpers

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{AxisAlignedBB, Vec3}

/**
 *
 *
 * @author CountryGamer
 */
object VecHelper {

	def toBB(vec: Vec3, startX: Int, startY: Int, startZ: Int): AxisAlignedBB = {
		AxisAlignedBB.getBoundingBox(
			startX, startY, startZ,
			startX + vec.xCoord,
			startY + vec.yCoord,
			startZ + vec.zCoord
		)
	}

	def writeToNBT(vec: Vec3, tag: NBTTagCompound): Unit = {
		tag.setDouble("x", vec.xCoord)
		tag.setDouble("y", vec.yCoord)
		tag.setDouble("z", vec.zCoord)

	}

	def readFromNBT(vec: Vec3, tag: NBTTagCompound): Unit = {
		vec.xCoord = tag.getDouble("x")
		vec.yCoord = tag.getDouble("y")
		vec.zCoord = tag.getDouble("z")

	}

}
