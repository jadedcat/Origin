package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.vec.V3O
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.util.{EnumFacing, MovingObjectPosition}
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World

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
	def getHeadPos(entity: EntityLivingBase): V3O = {
		val vec: V3O = new V3O(entity)
		vec.add(0, entity.getEyeHeight, 0)
		entity match {
			case player: EntityPlayer =>
				if (player.worldObj.isRemote)
					vec.add(0, -player.getDefaultEyeHeight, 0)
				else if (player.isInstanceOf[EntityPlayerMP] && player.isSneaking)
					vec.add(0, -0.08D, 0)
		}
		vec
	}

	/**
	 * Synonymous to codechicken.lib.raytracer.RayTracer.getEndVec(EntityPlayer player)
	 * @param entity
	 * @return
	 */
	def getCursorPosVec(
			entity: EntityLivingBase, reachDistance: Double, headVec: V3O
			): V3O = {
		// scale the look vector by the reach distance and translate it by the head/eye vector
		new V3O(entity.getLook(1.0F)) * reachDistance + headVec
	}

	def getCursorPosVec(entity: EntityLivingBase, reachDistance: Double): V3O = {
		this.getCursorPosVec(entity, reachDistance, this.getHeadPos(entity))
	}

	def getCursorPosVec(entity: EntityLivingBase): V3O = {
		entity match {
			case player: EntityPlayer =>
				this.getCursorPosVec(entity, Player.getReachDistance(player))
			case _ =>
				this.getCursorPosVec(entity, 5D)
		}
	}

	def getRaytracedBlock(world: World, entity: EntityLivingBase, reachLength: Double): V3O = {
		val head: V3O = this.getHeadPos(entity)
		val cursorPosVec: V3O = this.getCursorPosVec(entity, reachLength, head)
		val mop: MovingObjectPosition = world
				.rayTraceBlocks(head.toVec3(), cursorPosVec.toVec3(), false)
		if (mop == null) return null
		var blockX: Int = 0
		var blockY: Int = 0
		var blockZ: Int = 0
		var side: Int = 0
		if (mop.typeOfHit == MovingObjectType.BLOCK) {
			blockX = mop.getBlockPos().getX
			blockY = mop.getBlockPos().getY
			blockZ = mop.getBlockPos().getZ
			side = mop.sideHit.getIndex()
		}
		else {
			blockX = mop.hitVec.xCoord.asInstanceOf[Int]
			blockY = mop.hitVec.yCoord.asInstanceOf[Int]
			blockZ = mop.hitVec.zCoord.asInstanceOf[Int]
			side = 1
		}
		new V3O(blockX, blockY, blockZ) + EnumFacing.getFront(side)
	}

}
