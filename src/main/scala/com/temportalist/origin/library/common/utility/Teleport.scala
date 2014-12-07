package com.temportalist.origin.library.common.utility

import java.util.Random

import com.temportalist.origin.library.common.lib.TeleporterCore
import com.temportalist.origin.library.common.lib.vec.Vector3O
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
		val point: Vector3O = Cursor.getRaytracedBlock(
			entityPlayer.worldObj, entityPlayer, maxDistance
		)
		Teleport.toPoint(
			entityPlayer, point.add(0.5D, 0.0D, 0.5D)
		)
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

		var point: Vector3O = null
		var playerNewBB: AxisAlignedBB = null
		do {
			yVar += 1

			point = new Vector3O(
				MathFuncs.getRandomBetweenBounds(minRadius, maxRadius) +
						MathHelper.floor_double(player.posX) + 0.5,
				yVar,
				MathFuncs.getRandomBetweenBounds(minRadius, maxRadius) +
						MathHelper.floor_double(player.posZ) + 0.5
			)
			playerNewBB = AxisAlignedBB.getBoundingBox(
				point.x - halfWidth,
				point.y + heightOffset,
				point.z - halfWidth,
				point.x + halfWidth,
				point.y + player.height + heightOffset,
				point.z + halfWidth
			)

		}
		while (
			point.y > 0 && point.y < 128 &&
					!world.getCollidingBoundingBoxes(player, playerNewBB).isEmpty
		)

		if (!world.getCollidingBoundingBoxes(player, playerNewBB).isEmpty) {
			return this.toPointRandom(player, center, minRadius, maxRadius)
		}

		//point.yCoord += 1

		if (!this.canLandOnBlock(point.getBlock(world))) {
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
		this.toPoint(player, new Vector3O(x, y, z))
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
	def toPoint(player: EntityPlayer, point: Vector3O): Boolean = {
		// todo fall damage

		val event: EnderTeleportEvent = new EnderTeleportEvent(
			player, point.x_i(), point.y_i(), point.z_i(), 0.0F
		)
		if (MinecraftForge.EVENT_BUS.post(event)) return false

		val chunk: Chunk = player.worldObj.getChunkFromBlockCoords(point.x_i(), point.z_i())
		if (!chunk.isChunkLoaded) {
			player.worldObj.getChunkProvider.loadChunk(chunk.xPosition, chunk.zPosition)
		}

		player.setPositionAndUpdate(point.x_i(), point.y_i(), point.z_i())

		// todo particles

		true
	}

}
