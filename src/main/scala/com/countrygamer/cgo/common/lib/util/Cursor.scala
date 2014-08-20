package com.countrygamer.cgo.common.lib.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.{MathHelper, MovingObjectPosition, Vec3}
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
object Cursor {

	def getMOPFromPlayer(world: World, player: EntityPlayer,
			reachLength: Double): MovingObjectPosition = {
		val partialTicks: Float = 1.0F
		val checkLiquids: Boolean = true
		val checkCoordinates: Boolean = false

		val posVec: Vec3 = this.getEntityPosition(player, partialTicks)
		val translatedLookAndDistanceVec: Vec3 = this
				.getCursorPosition(player, partialTicks, reachLength)
		// ray traces blocks
		val mop: MovingObjectPosition = world.func_147447_a(
			posVec, translatedLookAndDistanceVec, checkLiquids, false, checkCoordinates)
		mop
	}

	def getEntityPosition(entity: EntityPlayer, partialTicks: Float): Vec3 = {
		val x: Double = entity.prevPosX +
				(entity.posX - entity.prevPosX) * partialTicks.asInstanceOf[Double]

		var yOffsetEyeHeight: Float = 0.0F
		// TODO This is test code!
		if (true) {
			yOffsetEyeHeight = if (entity.worldObj.isRemote) entity.getEyeHeight -
					entity.getDefaultEyeHeight
			else entity.getEyeHeight
		}
		else {
			yOffsetEyeHeight = 1.62F - entity.yOffset
		}

		val y: Double = entity.prevPosY +
				(entity.posY - entity.prevPosY) * partialTicks.asInstanceOf[Double] +
				yOffsetEyeHeight
		val z: Double = entity.prevPosZ +
				(entity.posZ - entity.prevPosZ) * partialTicks.asInstanceOf[Double]
		Vec3.createVectorHelper(x, y, z)
	}

	def getCursorPosition(entity: EntityPlayer, partialTicks: Float,
			reachLength: Double): Vec3 = {
		val yaw: Float = entity.prevRotationYaw +
				(entity.rotationYaw - entity.prevRotationYaw) * partialTicks
		val pitch: Float = entity.prevRotationPitch +
				(entity.rotationPitch - entity.prevRotationPitch) * partialTicks

		val yawCos: Float = MathHelper
				.cos(-yaw * 0.017453292F - java.lang.Math.PI.asInstanceOf[Float])
		val yawSin: Float = MathHelper
				.sin(-yaw * 0.017453292F - java.lang.Math.PI.asInstanceOf[Float])
		val pitchCos: Float = -MathHelper.cos(-pitch * 0.017453292F)
		val pitchSin: Float = MathHelper.sin(-pitch * 0.017453292F)

		val reachDistance: Double = if (reachLength < 0.0D) 5.0D else reachLength

		val x: Double = (yawSin * pitchCos).asInstanceOf[Double] * reachDistance
		val y: Double = pitchSin.asInstanceOf[Double] * reachDistance
		val z: Double = (yawCos * pitchCos).asInstanceOf[Double] * reachDistance

		this.getEntityPosition(entity, partialTicks).addVector(x, y, z)
	}

	def getNewCoordsFromSide(x: Int, y: Int, z: Int, side: Int): Array[Int] = {
		var x1: Int = x
		var y1: Int = y
		var z1: Int = z
		side match {
			case 0 =>
				y1 -= 1
			case 1 =>
				y1 += 1
			case 2 =>
				z1 -= 1
			case 3 =>
				z1 += 1
			case 4 =>
				x1 -= 1
			case 5 =>
				x1 += 1
		}
		Array[Int](x1, y1, z1)
	}

	def getBlockFromCursor(world: World, entity: EntityPlayer,
			reachLength: Double): MovingObjectPositionTarget = {
		val mop: MovingObjectPosition = Cursor.getMOPFromPlayer(world, entity, reachLength)
		if (mop == null) return null
		var blockX: Int = 0
		var blockY: Int = 0
		var blockZ: Int = 0
		var side: Int = 0
		if (mop.typeOfHit == MovingObjectType.BLOCK) {
			blockX = mop.blockX
			blockY = mop.blockY
			blockZ = mop.blockZ
			side = mop.sideHit
		}
		else {
			blockX = mop.hitVec.xCoord.asInstanceOf[Int]
			blockY = mop.hitVec.yCoord.asInstanceOf[Int]
			blockZ = mop.hitVec.zCoord.asInstanceOf[Int]
			side = 1
		}
		new MovingObjectPositionTarget(blockX, blockY, blockZ, side)
	}

}
