package com.temportalist.origin.library.common.utility

import java.util.Random

import com.temportalist.origin.library.common.lib.{TeleporterCore, Vec3Sided}
import net.minecraft.block.Block
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.util.{AxisAlignedBB, MathHelper, Vec3}
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.{World, WorldServer}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.EnderTeleportEvent

/**
 *
 *
 * @author TheTemportalist
 */
object Teleport {

	/**
	 * Teleports players to inputted dimensionID. Returns true if player is
	 * successfully teleported.
	 *
	 * @param entityPlayer The player being teleported
	 * @param dimID The dimension's ID
	 */
	def toDimension(entityPlayer: EntityPlayer, dimID: Int): Boolean = {
		if (entityPlayer.dimension != dimID) {
			entityPlayer match {
				case player: EntityPlayerMP =>
					val world: WorldServer = player.worldObj.asInstanceOf[WorldServer]
					if (player.ridingEntity == null && player.riddenByEntity == null) {
						val event: EnderTeleportEvent = new EnderTeleportEvent(
							player, player.posX, player.posY, player.posZ, 0.0F
						)
						if (MinecraftForge.EVENT_BUS.post(event)) return false
						player.mcServer.getConfigurationManager.transferPlayerToDimension(
							player, dimID, new TeleporterCore(world)
						)
						if (player.dimension == dimID) {
							return true
						}
					}
				case _ =>
			}
		}
		false
	}

	/**
	 * Teleports player based on where their crosshair lays. Max distance of 500.0D (500 blocks)
	 *
	 * @param entityPlayer
	 */
	def toCursorPosition(entityPlayer: EntityPlayer): Boolean = {
		this.toCursorPosition(entityPlayer, 500.0D)
	}

	/**
	 * Teleports player based on where their crosshair lays.
	 *
	 * @param entityPlayer
	 * @param maxDistance
	 */
	def toCursorPosition(entityPlayer: EntityPlayer, maxDistance: Double): Boolean = {
		entityPlayer match {
			case player: EntityPlayerMP =>
				val point: Vec3Sided = Cursor.getBlockFromCursor(
					player.worldObj, player, maxDistance
				)
				if (point != null) {
					val pos: Vec3 = Cursor.getNewCoordsFromSide(point)
					if (pos != null) {
						return Teleport.toPoint(
							player, pos.addVector(0.5D, 0.0D, 0.5D)
						)
					}
				}
			case _ =>
		}
		false
	}

	def toPointRandom(player: EntityPlayer, minRange: Int, maxRange: Int): Boolean = {
		this.toPointRandom(
			player, Vec3.createVectorHelper(player.posX, player.posY, player.posZ),
			minRange, maxRange
		)
	}

	def toPointRandom(player: EntityPlayer, center: Vec3, minRadius: Int,
			maxRadius: Int): Boolean = {
		val world: World = player.worldObj
		val random: Random = new Random

		val halfWidth: Double = player.width / 2
		val heightOffset: Double = player.ySize - player.yOffset
		var yVar: Int = random.nextInt(128)

		var point: Vec3 = null
		var playerNewBB: AxisAlignedBB = null
		do {
			yVar += 1

			point = Vec3.createVectorHelper(
				MathFuncs.getRandomBetweenBounds(minRadius, maxRadius) +
						MathHelper.floor_double(player.posX) + 0.5,
				yVar,
				MathFuncs.getRandomBetweenBounds(minRadius, maxRadius) +
						MathHelper.floor_double(player.posZ) + 0.5
			)
			playerNewBB = AxisAlignedBB.getBoundingBox(
				point.xCoord - halfWidth,
				point.yCoord + heightOffset,
				point.zCoord - halfWidth,
				point.xCoord + halfWidth,
				point.yCoord + player.height + heightOffset,
				point.zCoord + halfWidth
			)

		}
		while (
			point.yCoord > 0 && point.yCoord < 128 &&
					!world.getCollidingBoundingBoxes(player, playerNewBB).isEmpty
		)

		if (!world.getCollidingBoundingBoxes(player, playerNewBB).isEmpty) {
			return this.toPointRandom(player, center, minRadius, maxRadius)
		}

		//point.yCoord += 1

		if (!this.canLandOnBlock(world.getBlock(
			MathHelper.floor_double(point.xCoord),
			MathHelper.floor_double(point.yCoord),
			MathHelper.floor_double(point.zCoord)
		))) {
			return this.toPointRandom(player, center, minRadius, maxRadius)
		}

		this.toPoint(player, point)
	}

	private def canLandOnBlock(block: Block): Boolean = {
		/*
		block == Blocks.lava ||
				block == Blocks.flowing_lava ||
				block == Blocks.water ||
				block == Blocks.flowing_water
		*/
		!block.getMaterial.isLiquid
	}

	/**
	 * Teleports player to the xyz parameter coordinates. If fallDamage is
	 * false, height player was at before teleportation will not be calculated
	 * into fall damage. This does not apply to post teleportation fall damage.
	 * If particles is true, will spawn particles after teleportation.
	 *
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	def toPoint(player: EntityPlayer, x: Double, y: Double, z: Double): Boolean = {
		this.toPoint(player, Vec3.createVectorHelper(x, y, z))
	}

	/**
	 * Teleports player to the xyz parameter coordinates. If fallDamage is
	 * false, height player was at before teleportation will not be calculated
	 * into fall damage. This does not apply to post teleportation fall damage.
	 * If particles is true, will spawn particles after teleportation.
	 *
	 * @param player
	 * @param point
	 */
	def toPoint(player: EntityPlayer, point: Vec3): Boolean = {
		// todo fall damage

		val event: EnderTeleportEvent = new EnderTeleportEvent(
			player, point.xCoord, point.yCoord, point.zCoord, 0.0F
		)
		if (MinecraftForge.EVENT_BUS.post(event)) return false

		val chunk: Chunk = player.worldObj.getChunkFromBlockCoords(
			point.xCoord.asInstanceOf[Int], point.zCoord.asInstanceOf[Int]
		)
		if (!chunk.isChunkLoaded) {
			player.worldObj.getChunkProvider.loadChunk(chunk.xPosition, chunk.zPosition)
		}

		player.setPositionAndRotation(
			point.xCoord, point.yCoord, point.zCoord,
			player.rotationYaw, player.rotationPitch
		)

		// todo particles

		true
	}

}
