package com.countrygamer.cgo.common.lib.util

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.{MathHelper, MovingObjectPosition, Vec3}
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
object Cursor {

	def rayTrace(player: EntityPlayer, reachDistance: Double): MovingObjectPosition = {
		val partialTicks: Float = 1.0F
		val checkLiquids: Boolean = true
		val checkEntities: Boolean = true

		val position: Vec3 = this.getPosition(player, partialTicks)
		val cursor: Vec3 = this.getCursor(player, reachDistance, partialTicks)

		player.worldObj.func_147447_a(position, cursor, checkLiquids, false, checkEntities)
	}

	def getPosition(entity: EntityLivingBase, partialTicks: Float): Vec3 = {
		val x: Double = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks
		var y: Double = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks
		val z: Double = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks
		entity match {
			case player: EntityPlayer =>
				if (player.worldObj.isRemote) {
					y = y + (player.getEyeHeight - player.getDefaultEyeHeight)
				}
				else {
					y = y + player.getEyeHeight
					if (player.isInstanceOf[EntityPlayerMP] && player.isSneaking)
						y = y - 0.08D
				}
			case _ =>
		}
		Vec3.createVectorHelper(x, y, z)
	}

	def getCursor(entity: EntityLivingBase, reachDistance: Double, partialTicks: Float): Vec3 = {
		val pitch: Float = entity.prevRotationPitch +
				(entity.prevRotationPitch - entity.rotationPitch) * partialTicks
		val yaw: Float = entity.prevRotationYaw +
				(entity.prevRotationYaw - entity.rotationYaw * partialTicks)

		val cosYaw: Float = MathHelper
				.cos(-yaw * 0.017453292F - java.lang.Math.PI.asInstanceOf[Float])
		val sinYaw: Float = MathHelper
				.sin(-yaw * 0.017453292F - java.lang.Math.PI.asInstanceOf[Float])
		val cosPitch: Float = -MathHelper.cos(-pitch * 0.017453292F)
		val sinPitch: Float = MathHelper.sin(-pitch * 0.017453292F)
		val cursor: Vec3 = Vec3.createVectorHelper(
			(sinYaw * cosPitch).asInstanceOf[Double],
			sinPitch.asInstanceOf[Double],
			(cosYaw * cosPitch).asInstanceOf[Double]
		)
		this.getPosition(entity, partialTicks)
				.addVector(cursor.xCoord * reachDistance, cursor.yCoord * reachDistance,
		            cursor.zCoord * reachDistance)
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
		val mop: MovingObjectPosition = Cursor.rayTrace(entity, reachLength)
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
