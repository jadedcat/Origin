package com.countrygamer.cgo.common.lib.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import java.util.HashMap;
import java.util.Random;

public class UtilVector {

	public static int getQuad(double offset_X, double offset_Z) {
		if (offset_X > 0 && offset_Z >= 0)
			return 1;
		else if (offset_X <= 0 && offset_Z > 0)
			return 2;
		else if (offset_X < 0 && offset_Z <= 0)
			return 3;
		else if (offset_X >= 0 && offset_Z < 0)
			return 4;
		else
			return 0;
	}

	public static double getRotationFromCoords(double pivotX, double pivotZ, double targetX,
			double targetZ) {
		double offset_X = -(pivotX - targetX);
		double offset_Z = -(pivotZ - targetZ);

		double rotaion_radians = java.lang.Math.atan(offset_Z / offset_X);
		double rotation_deg = rotaion_radians * 180 / java.lang.Math.PI;

		int quad = getQuad(offset_X, offset_Z);
		if (quad == 3 || quad == 4) {
			rotation_deg += 180;
		}

		return rotation_deg;
	}

	public static HashMap<String, Integer> getCardinalByBlockSide() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		ret.put("bottom", 0);
		ret.put("top", 1);
		ret.put("north", 2);
		ret.put("south", 3);
		ret.put("west", 4);
		ret.put("east", 5);
		return ret;
	}

	// Teleportation

	/**
	 * Teleports players to inputted dimensionID. Returns true if player is
	 * successfully teleported.
	 *
	 * @param player
	 * @param dimensionID
	 */
	public static boolean teleportPlayerToDimension(EntityPlayer player, int dimensionID) {
		if (player.dimension != dimensionID) {

			// Side side = FMLCommonHandler.instance().getEffectiveSide();
			// if (side == Side.SERVER) {
			if (player instanceof EntityPlayerMP) {
				WorldServer ws = (WorldServer) player.worldObj;
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if (player.ridingEntity == null && player.riddenByEntity == null) {
					EnderTeleportEvent event = new EnderTeleportEvent(playerMP, playerMP.posX,
							playerMP.posY, playerMP.posZ, 0.0F);
					if (MinecraftForge.EVENT_BUS.post(event))
						return false;

					playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP,
							dimensionID, new TeleporterCore(ws));
					if (player.dimension == dimensionID)
						return true;
				}
			}
		}
		return false;
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
	 * @param fallDamage
	 * @param particles
	 */
	public static void teleportPlayer(EntityPlayer player, double x, double y, double z,
			boolean fallDamage, boolean particles) {
		if (!fallDamage)
			player.fallDistance = 0.0F;

		EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0.0F);
		if (MinecraftForge.EVENT_BUS.post(event))
			return;

		// Set the location of the player, on the final position.
		//player.setPositionAndUpdate(x, y, z);
		player.setPositionAndRotation(x, y, z, player.rotationYaw, player.rotationPitch);
		//player.setAngles(player.rotationYaw, player.rotationPitch);
		// FMLLog.info("Succesfully teleported to: "+(int)player.posX+" "+(int)player.posY+" "+(int)player.posZ);

		if (particles) {
			Random rand = new Random();
			int l = 128;
			for (int j1 = 0; j1 < l; j1++) {
				double d6 = (double) j1 / ((double) l - 1.0D);
				//float f = (rand.nextFloat() - 0.5F) * 0.2F;
				//float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				//float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = x + (player.posX - x) * d6 + (rand.nextDouble() - 0.5D)
						* (double) player.width * 2D;
				double d8 = y + (player.posY - y) * d6 + rand.nextDouble()
						* (double) player.height;
				double d9 = z + (player.posZ - z) * d6 + (rand.nextDouble() - 0.5D)
						* (double) player.width * 2D;
				player.worldObj.spawnParticle("portal", x, y, z, d7, d8, d9);

			}
		}
	}

	/**
	 * Teleports players within a defined range from the centerX and centerZ
	 * parameters. See the other teleportPlayer method for descriptions of
	 * fallDamage and particles parameters.
	 *
	 * @param player
	 * @param minimumRange
	 * @param maximumRange
	 * @param centerX
	 * @param centerZ
	 * @param fallDamage
	 * @param particles
	 */
	public static void teleportPlayer(EntityPlayer player, int minimumRange, int maximumRange,
			double centerX, double centerZ, boolean fallDamage, boolean particles) {
		double[] newPos = UtilVector.teleportBase(player.worldObj, player, minimumRange,
				maximumRange, centerX, centerZ);
		// newPos[1] -= 2;
		UtilVector.teleportPlayer(player, newPos[0], newPos[1], newPos[2], fallDamage, particles);
	}

	/**
	 * Calculates a valid set of coordinates within a defined range from the
	 * centerX and centerZ parameters. Returns a valid set as indexes of a
	 * double array. Valid coordinates are arranged in the returned array as
	 * follows: X = array[0], Y = array[1], Z = array[2]
	 *
	 * @param world
	 * @param player
	 * @param minimumRange
	 * @param maximumRange
	 * @param x
	 * @param z
	 * @return
	 */
	public static double[] teleportBase(World world, EntityPlayer player, int minimumRange,
			int maximumRange, double x, double z) {
		Random rand = new Random();

		int range = maximumRange - minimumRange;
		if (range < 0) {
			range = -range;
		}

		//System.out.println(range);

		int offsetX = rand.nextInt(range) + minimumRange;
		int offsetZ = rand.nextInt(range) + minimumRange;

		// Center the values on a block, to make the boundingbox calculations
		// match less.
		double newX = MathHelper.floor_double(x) + offsetX + 0.5;
		double newY = rand.nextInt(128);
		double newZ = MathHelper.floor_double(z) + offsetZ + 0.5;

		double bbMinX = newX - player.width / 2.0;
		double bbMinY = newY - player.yOffset + player.ySize;
		double bbMinZ = newZ - player.width / 2.0;
		double bbMaxX = newX + player.width / 2.0;
		double bbMaxY = newY - player.yOffset + player.ySize + player.height;
		double bbMaxZ = newZ + player.width / 2.0;

		// FMLLog.info("Teleporting from: "+(int)player.posX+" "+(int)player.posY+" "+(int)player.posZ);
		// FMLLog.info("Teleporting with offsets: "+offsetX+" "+newY+" "+offsetZ);
		// FMLLog.info("Starting BB Bounds: "+bbMinX+" "+bbMinY+" "+bbMinZ+" "+bbMaxX+" "+bbMaxY+" "+bbMaxZ);

		// Use a testing boundingBox, so we don't have to move the player around
		// to test if it is a valid location
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(bbMinX, bbMinY, bbMinZ, bbMaxX,
				bbMaxY, bbMaxZ);

		// Make sure you are trying to teleport to a loaded chunk.
		Chunk teleportChunk = world.getChunkFromBlockCoords((int) newX, (int) newZ);
		if (!teleportChunk.isChunkLoaded) {
			world.getChunkProvider().loadChunk(teleportChunk.xPosition, teleportChunk.zPosition);
		}

		// Move up, until nothing intersects the player anymore
		while (newY > 0 && newY < 128
				&& !world.getCollidingBoundingBoxes(player, boundingBox).isEmpty()) {
			++newY;

			bbMinY = newY - player.yOffset + player.ySize;
			bbMaxY = newY - player.yOffset + player.ySize + player.height;

			boundingBox.setBounds(bbMinX, bbMinY, bbMinZ, bbMaxX, bbMaxY, bbMaxZ);

			// FMLLog.info("Failed to teleport, retrying at height: "+(int)newY);
		}

		// If we could place it, could we have placed it lower? To prevent
		// teleports really high up.

		do {
			--newY;

			bbMinY = newY - player.yOffset + player.ySize;
			bbMaxY = newY - player.yOffset + player.ySize + player.height;

			boundingBox.setBounds(bbMinX, bbMinY, bbMinZ, bbMaxX, bbMaxY, bbMaxZ);

			// FMLLog.info("Trying a lower teleport at height: "+(int)newY); }
		} while (newY > 0 && newY < 128
				&& world.getCollidingBoundingBoxes(player, boundingBox).isEmpty());

		// Set Y one higher, as the last lower placing test failed.
		++newY;

		// Check for placement in lava
		// NOTE: This can potentially hang the game indefinitely, due to random
		// recursion
		// However this situation is highly unlikelely
		// My advice: Dont encounter Weeping Angels in seas of lava
		// NOTE: This can theoretically still teleport you to a block of lava
		// with air underneath, but gladly lava spreads ;)
		Block block = world.getBlock(MathHelper.floor_double(newX), MathHelper.floor_double(newY),
				MathHelper.floor_double(newZ));
		if (block == Blocks.lava || block == Blocks.flowing_lava || block == Blocks.water
				|| block == Blocks.flowing_water) {
			return UtilVector.teleportBase(world, player, minimumRange, maximumRange, x, z);

		}
		return new double[] {
				newX, newY, newZ
		};
	}

	/**
	 * Teleports player based on where their crosshair lays.
	 *
	 * @param world
	 * @param player
	 */
	public static void teleportVector(World world, EntityPlayer player) {
		UtilVector.teleportVector(world, player, 500.0D);
	}

	/**
	 * Teleports player based on where their crosshair lays.
	 *
	 * @param world
	 * @param player
	 * @param maxDistance
	 */
	public static void teleportVector(World world, EntityPlayer player, double maxDistance) {
		if (!world.isRemote) {
			MovingObjectPositionTarget coords = Cursor
					.getBlockFromCursor(world, player, maxDistance);

			if (coords == null) {
				return;
			}

			int[] newCoords = Cursor.getNewCoordsFromSide(coords.x(), coords.y(), coords.z(),
					coords.side());
			coords = new MovingObjectPositionTarget(newCoords[0], newCoords[1], newCoords[2],
					coords.side());
			UtilVector.teleportPlayer(player, coords.x() + 0.5, coords.y(), coords.z() + 0.5,
					false, false);
		}
	}

}
