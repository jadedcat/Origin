package com.temportalist.origin.library.common.utility

import codechicken.lib.raytracer.RayTracer
import com.temportalist.origin.library.common.lib.vec.Vector3b
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

/**
 *
 *
 * @author TheTemportalist
 */
object Cursor {

	/**
	 * Synonymous to codechicken.lib.raytracer.RayTracer.getCorrectedHeadVec(EntityPlayer player)
	 * @param entity
	 * @return
	 */
	def getHeadPos(entity: EntityLivingBase): Vector3b = {
		val vec: Vector3b = new Vector3b(entity)
		vec.translate(0, entity.getEyeHeight, 0)
		entity match {
			case player: EntityPlayer =>
				if (player.worldObj.isRemote)
					vec.translate(0, -player.getDefaultEyeHeight, 0)
				else if (player.isInstanceOf[EntityPlayerMP] && player.isSneaking)
					vec.translate(0, -0.08D, 0)
		}
		vec
	}

	/**
	 * Synonymous to codechicken.lib.raytracer.RayTracer.getEndVec(EntityPlayer player)
	 * @param entity
	 * @return
	 */
	def getCursorPosVec(
			entity: EntityLivingBase, reachDistance: Double, headVec: Vector3b
			): Vector3b = {
		// scale the look vector by the reach distance and translate it by the head/eye vector
		new Vector3b(entity.getLook(1.0F)).scale(reachDistance).translate(headVec)
	}

	def getCursorPosVec(entity: EntityLivingBase, reachDistance: Double): Vector3b = {
		this.getCursorPosVec(entity, reachDistance, this.getHeadPos(entity))
	}

	def getCursorPosVec(entity: EntityLivingBase): Vector3b = {
		entity match {
			case player: EntityPlayer =>
				this.getCursorPosVec(entity, RayTracer.getBlockReachDistance(player))
			case _ =>
				this.getCursorPosVec(entity, 5D)
		}
	}

	def getRaytracedBlock(world: World, entity: EntityLivingBase, reachLength: Double): Vector3b = {
		val head: Vector3b = this.getHeadPos(entity)
		val cursorPosVec: Vector3b = this.getCursorPosVec(entity, reachLength, head)
		val mop: MovingObjectPosition = world
				.rayTraceBlocks(head.toVec3D, cursorPosVec.toVec3D, false)
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
		new Vector3b(blockX, blockY, blockZ).translate(ForgeDirection.getOrientation(side))
	}

}
