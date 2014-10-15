package com.countrygamer.cgo.library.common.utility

import com.countrygamer.cgo.library.common.lib.Vec3Sided
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.{MathHelper, MovingObjectPosition, Vec3}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

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

	def getNewCoordsFromSide(x: Int, y: Int, z: Int, side: Int): Vec3 = {
		this.getNewCoordsFromSide(new Vec3Sided(x, y, z, side))
	}

	def getNewCoordsFromSide(vec: Vec3Sided): Vec3 = {
		val direction: ForgeDirection = ForgeDirection.getOrientation(vec.side)
		if (direction != null) {
			Vec3.createVectorHelper(
				vec.xCoord + direction.offsetX,
				vec.yCoord + direction.offsetY,
				vec.zCoord + direction.offsetZ
			)
		}
		else {
			null
		}
	}

	def getBlockFromCursor(world: World, entity: EntityPlayer,
			reachLength: Double): Vec3Sided = {
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
		new Vec3Sided(blockX, blockY, blockZ, side)
	}

}
