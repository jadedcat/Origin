package CountryGamer_Core.lib;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import CountryGamer_Core.CG_Core;
import cpw.mods.fml.common.Loader;

public class CoreUtil {

	public static boolean isModLoaded(String sourceModid, String targetModid) {
		if (Loader.isModLoaded(targetModid)) {
			try {
				System.out.println(sourceModid + ": " + targetModid
						+ " mod is loaded");
				return true;
			} catch (Exception e) {
				System.err.println(sourceModid + ": Could not load "
						+ targetModid + " mod");
				e.printStackTrace(System.err);
			}
		}
		return false;
	}

	public static void teleportPlayerToDimension(EntityPlayer player,
			int dimensionID) {
		if (player.dimension != dimensionID) {
			// Side side = FMLCommonHandler.instance().getEffectiveSide();
			// if (side == Side.SERVER) {
			if (player instanceof EntityPlayerMP) {
				WorldServer ws = (WorldServer) player.worldObj;
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if (player.ridingEntity == null
						&& player.riddenByEntity == null) {
					playerMP.mcServer.getConfigurationManager()
							.transferPlayerToDimension(playerMP, dimensionID,
									new TeleporterCore(ws));
					if (player.dimension == dimensionID)
						if (CG_Core.DEBUG)
							CG_Core.log.info("Successfully teleported to dim "
									+ dimensionID);
				} else if (CG_Core.DEBUG)
					CG_Core.log.info("Riding entity stuff");
			} else if (CG_Core.DEBUG)
				CG_Core.log.info("Not PlayerMP");
			// } else if (WeepingAngelsMod.DEBUG)
			// WeepingAngelsMod.log.info("Side Not Server");
		} else if (CG_Core.DEBUG)
			CG_Core.log.info("Player and destination dim are equal");
	}

	public static void teleportPlayer(EntityPlayer player, double x, double y,
			double z, boolean fallDamage, boolean particles) {
		if (!fallDamage)
			player.fallDistance = 0.0F;

		// Set the location of the player, on the final position.
		player.setPositionAndUpdate(x, y, z);
		player.setAngles(player.rotationYaw, player.rotationPitch);
		// FMLLog.info("Succesfully teleported to: "+(int)player.posX+" "+(int)player.posY+" "+(int)player.posZ);

		if (particles) {
			Random rand = new Random();
			double d3 = x;
			double d4 = y;
			double d5 = z;
			int l = 128;
			for (int j1 = 0; j1 < l; j1++) {
				double d6 = (double) j1 / ((double) l - 1.0D);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (player.posX - d3) * d6
						+ (rand.nextDouble() - 0.5D) * (double) player.width
						* 2D;
				double d8 = d4 + (player.posY - d4) * d6 + rand.nextDouble()
						* (double) player.height;
				double d9 = d5 + (player.posZ - d5) * d6
						+ (rand.nextDouble() - 0.5D) * (double) player.width
						* 2D;
				player.worldObj.spawnParticle("portal", d7, d8 - 1, d9, f, f1,
						f2);
			}
		}
	}

	public static void teleportPlayer(EntityPlayer player, int minimumRange,
			int maximumRange, boolean fallDamage, boolean particles) {
		double[] newPos = CoreUtil.teleportBase(player.worldObj, player,
				minimumRange, maximumRange);
		newPos[1] -= 2;
		CoreUtil.teleportPlayer(player, newPos[0], newPos[1], newPos[2],
				fallDamage, particles);
	}

	private static double[] teleportBase(World world, EntityPlayer player,
			int minimumRange, int maximumRange) {
		Random rand = new Random();

		int rangeDifference = 2 * (maximumRange - minimumRange);
		int offsetX = rand.nextInt(rangeDifference) - rangeDifference / 2
				+ minimumRange;
		int offsetZ = rand.nextInt(rangeDifference) - rangeDifference / 2
				+ minimumRange;

		// Center the values on a block, to make the boundingbox
		// calculations match less.
		double newX = MathHelper.floor_double(player.posX) + offsetX + 0.5;
		double newY = rand.nextInt(128);
		double newZ = MathHelper.floor_double(player.posZ) + offsetZ + 0.5;

		double bbMinX = newX - player.width / 2.0;
		double bbMinY = newY - player.yOffset + player.ySize;
		double bbMinZ = newZ - player.width / 2.0;
		double bbMaxX = newX + player.width / 2.0;
		double bbMaxY = newY - player.yOffset + player.ySize + player.height;
		double bbMaxZ = newZ + player.width / 2.0;

		// FMLLog.info("Teleporting from: "+(int)player.posX+" "+(int)player.posY+" "+(int)player.posZ);
		// FMLLog.info("Teleporting with offsets: "+offsetX+" "+newY+" "+offsetZ);
		// FMLLog.info("Starting BB Bounds: "+bbMinX+" "+bbMinY+" "+bbMinZ+" "+bbMaxX+" "+bbMaxY+" "+bbMaxZ);

		// Use a testing boundingBox, so we don't have to move the player
		// around to test if it is a valid location
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(bbMinX,
				bbMinY, bbMinZ, bbMaxX, bbMaxY, bbMaxZ);

		// Make sure you are trying to teleport to a loaded chunk.
		Chunk teleportChunk = world.getChunkFromBlockCoords((int) newX,
				(int) newZ);
		if (!teleportChunk.isChunkLoaded) {
			world.getChunkProvider().loadChunk(teleportChunk.xPosition,
					teleportChunk.zPosition);
		}

		// Move up, until nothing intersects the player anymore
		while (newY > 0
				&& newY < 128
				&& !world.getCollidingBoundingBoxes(player, boundingBox)
						.isEmpty()) {
			++newY;

			bbMinY = newY - player.yOffset + player.ySize;
			bbMaxY = newY - player.yOffset + player.ySize + player.height;

			boundingBox.setBounds(bbMinX, bbMinY, bbMinZ, bbMaxX, bbMaxY,
					bbMaxZ);

			// FMLLog.info("Failed to teleport, retrying at height: "+(int)newY);
		}

		// If we could place it, could we have placed it lower? To prevent
		// teleports really high up.
		do {
			--newY;

			bbMinY = newY - player.yOffset + player.ySize;
			bbMaxY = newY - player.yOffset + player.ySize + player.height;

			boundingBox.setBounds(bbMinX, bbMinY, bbMinZ, bbMaxX, bbMaxY,
					bbMaxZ);

			// FMLLog.info("Trying a lower teleport at height: "+(int)newY);
		} while (newY > 0
				&& newY < 128
				&& world.getCollidingBoundingBoxes(player, boundingBox)
						.isEmpty());
		// Set Y one higher, as the last lower placing test failed.
		++newY;

		// Check for placement in lava
		// NOTE: This can potentially hang the game indefinitely, due to
		// random recursion
		// However this situation is highly unlikelely
		// My advice: Dont encounter Weeping Angels in seas of lava
		// NOTE: This can theoretically still teleport you to a block of
		// lava with air underneath, but gladly lava spreads ;)
		int blockIdUnder = world.getBlockId(MathHelper.floor_double(newX),
				MathHelper.floor_double(newY), MathHelper.floor_double(newZ));
		int blockIdAt = world.getBlockId(MathHelper.floor_double(newX),
				MathHelper.floor_double(newY + 1),
				MathHelper.floor_double(newZ));
		int blockIdAbove = world.getBlockId(MathHelper.floor_double(newX),
				MathHelper.floor_double(newY + 2),
				MathHelper.floor_double(newZ));
		ArrayList<Integer> blocks = new ArrayList<Integer>();
		blocks.add(Block.lavaStill.blockID);
		blocks.add(Block.lavaMoving.blockID);
		blocks.add(Block.waterStill.blockID);
		blocks.add(Block.waterMoving.blockID);
		if (blocks.contains(blockIdUnder) || blocks.contains(blockIdAt)
				|| blocks.contains(blockIdAbove)) {
			return CoreUtil.teleportBase(world, player, minimumRange,
					maximumRange);
		}
		// if (world.getBlockId((int)newX, (int)newY, (int)newZ) == 0 ||
		// world.getBlockId((int)newX, (int)newY + 1, (int)newZ) != 0) {
		// return Util.teleportBase(world, player);
		// }
		return new double[] { newX, newY, newZ };
	}

}
