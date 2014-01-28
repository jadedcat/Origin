package CountryGamer_Core.lib;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import CountryGamer_Core.CG_Core;
import WeepingAngels.World.Structure.ComponentAngelDungeon;
import cpw.mods.fml.common.Loader;

public class CoreUtil {
	
	public static String	configGeneral		= "General";
	public static String	configItemId		= "Item IDs";
	public static String	configBlockId		= "Block IDs";
	public static String	configAchievement	= "Achievement IDs";
	public static String	configAddon			= "Addons";
	
	public static int getAndComment(Configuration config, String cate,
			String name, String comment, int value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getInt();
	}
	
	public static String getAndComment(Configuration config, String cate,
			String name, String comment, String value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getString();
	}
	
	public static boolean getAndComment(Configuration config, String cate,
			String name, String comment, boolean value) {
		Property property = config.get(cate, name, value);
		if (!comment.equals(""))
			property.comment = comment;
		return property.getBoolean(false);
	}
	
	/**
	 * Find new id
	 * 
	 * @return
	 */
	public static int getUniqueEntityId() {
		int entityid = 0;
		do {
			entityid += 1;
		} while (EntityList.getStringFromID(entityid) != null);
		return entityid;
	}
	
	/**
	 * Check for loaded mod
	 * 
	 * @param sourceModid
	 * @param targetModid
	 * @return
	 */
	public static boolean isModLoaded(String sourceModid, String targetModid) {
		if (Loader.isModLoaded(targetModid)) {
			try {
				// System.out.println(sourceModid + ": " + targetModid
				// + " mod is loaded");
				return true;
			} catch (Exception e) {
				// System.err.println(sourceModid + ": Could not load "
				// + targetModid + " mod");
				e.printStackTrace(System.err);
			}
		}
		return false;
	}
	
	// Teleportation
	/**
	 * Teleports players to inputted dimensionID. Returns true if player is
	 * successfully teleported.
	 * 
	 * @param player
	 * @param dimensionID
	 */
	public static boolean teleportPlayerToDimension(EntityPlayer player,
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
						return true;
				} else
					if (CG_Core.DEBUG)
						CG_Core.log.info("Riding entity stuff");
			} else
				if (CG_Core.DEBUG)
					CG_Core.log.info("Not PlayerMP");
			// } else if (WeepingAngelsMod.DEBUG)
			// WeepingAngelsMod.log.info("Side Not Server");
		} else
			if (CG_Core.DEBUG)
				CG_Core.log.info("Player and destination dim are equal");
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
				player.worldObj.spawnParticle("portal", d3, d4, d5, d7, d8, d9);
				
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
	public static void teleportPlayer(EntityPlayer player, int minimumRange,
			int maximumRange, double centerX, double centerZ,
			boolean fallDamage, boolean particles) {
		double[] newPos = CoreUtil.teleportBase(player.worldObj, player,
				minimumRange, maximumRange, centerX, centerZ);
		// newPos[1] -= 2;
		CoreUtil.teleportPlayer(player, newPos[0], newPos[1], newPos[2],
				fallDamage, particles);
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
	public static double[] teleportBase(World world, EntityPlayer player,
			int minimumRange, int maximumRange, double x, double z) {
		Random rand = new Random();
		int rangeDifference = 2 * (maximumRange - minimumRange);
		int offsetX = rand.nextInt(rangeDifference) - rangeDifference / 2
				+ minimumRange;
		int offsetZ = rand.nextInt(rangeDifference) - rangeDifference / 2
				+ minimumRange;
		
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
			
			// FMLLog.info("Trying a lower teleport at height: "+(int)newY); }
		} while (newY > 0
				&& newY < 128
				&& world.getCollidingBoundingBoxes(player, boundingBox)
						.isEmpty());
		
		// Set Y one higher, as the last lower placing test failed.
		++newY;
		
		// Check for placement in lava
		// NOTE: This can potentially hang the game indefinitely, due to random
		// recursion
		// However this situation is highly unlikelely
		// My advice: Dont encounter Weeping Angels in seas of lava
		// NOTE: This can theoretically still teleport you to a block of lava
		// with air underneath, but gladly lava spreads ;)
		int blockId = world.getBlockId(MathHelper.floor_double(newX),
				MathHelper.floor_double(newY), MathHelper.floor_double(newZ));
		if (blockId == Block.lavaStill.blockID
				|| blockId == Block.lavaMoving.blockID
				|| blockId == Block.waterStill.blockID
				|| blockId == Block.waterMoving.blockID) {
			return CoreUtil.teleportBase(world, player, minimumRange,
					maximumRange, x, z);
			
		}
		return new double[] { newX, newY, newZ };
	}
	
	// World set and Component Setting methods for block placement
	
	public static void placeBlock(World world, int x, int y, int z,
			int blockID, int meta, ComponentAngelDungeon com,
			StructureBoundingBox box) {
		boolean normalWorld = com == null || box == null;
		if (normalWorld) { // regular
			world.setBlock(x, y, z, blockID, meta, 3);
		} else { // component
			if (blockID == Block.stairsCobblestone.blockID
					|| blockID == Block.ladder.blockID
					|| blockID == Block.trapdoor.blockID) {
				com.placeBlockAtCurrentPosition(world, blockID,
						com.getMetadataWithOffset(blockID, meta), x, y, z, box);
			} else
				com.placeBlockAtCurrentPosition(world, blockID, meta, x, y, z,
						box);
		}
	}
	
	public static void fillBlocks(World world, int minX, int minY, int minZ,
			int maxX, int maxY, int maxZ, int blockID, int meta,
			ComponentAngelDungeon com, StructureBoundingBox box) {
		boolean normalWorld = com == null || box == null;
		for (int k2 = minY; k2 <= maxY; ++k2) {
			for (int l2 = minX; l2 <= maxX; ++l2) {
				for (int i3 = minZ; i3 <= maxZ; ++i3) {
					if (normalWorld) { // regular
						world.setBlock(l2, k2, i3, blockID, meta, 3);
					} else { // component
						com.placeBlockAtCurrentPosition(world, blockID, meta,
								l2, k2, i3, box);
					}
				}
			}
		}
	}
	
	public static void fillVariedStoneBlocks(World world, int minX, int minY,
			int minZ, int maxX, int maxY, int maxZ, ComponentAngelDungeon com,
			StructureBoundingBox box) {
		boolean normalWorld = com == null || box == null;
		int blockID = Block.stoneBrick.blockID;
		for (int k2 = minY; k2 <= maxY; ++k2) {
			for (int l2 = minX; l2 <= maxX; ++l2) {
				for (int i3 = minZ; i3 <= maxZ; ++i3) {
					int meta = CoreUtil.getStoneBrickMeta();
					if (normalWorld) { // regular
						world.setBlock(l2, k2, i3, blockID, meta, 3);
					} else { // component
						com.placeBlockAtCurrentPosition(world, blockID, meta,
								l2, k2, i3, box);
					}
				}
			}
		}
	}
	
	public static int getStoneBrickMeta() {
		int meta = 0;
		int chance = (new Random()).nextInt(100);
		if (chance <= 45) {
			meta = 1;
			if (chance <= 10)
				meta = 2;
		}
		return meta;
	}
	
	public static boolean breakBlockAsPlayer(World world, EntityPlayer player,
			int x, int y, int z, int blockID) {
		if (player == null || world == null)
			return false;
		WorldClient worldclient = Minecraft.getMinecraft().theWorld;
		worldclient.playAuxSFX(2001, x, y, z,
				blockID + (worldclient.getBlockMetadata(x, y, z) << 12));
		
		int meta = world.getBlockMetadata(x, y, z);
		Block block = Block.blocksList[blockID];
		world.setBlockToAir(x, y, z);
		block.onBlockDestroyedByPlayer(world, x, y, z, meta);
		
		return true;
	}
	
	// OTHER
	/**
	 * Is int positive or negative
	 * 
	 * @param i
	 * @return
	 */
	public static int posOrNeg(int i) {
		if (i == 0) {
			System.err.print("Parameter is neither positive nor negative");
			return 1;
		} else
			if (i >> 31 != 0)
				return -1;
			else
				return 1;
	}
	
	/**
	 * Get direction player is facing. Returns an integer representing the
	 * cardinal direction and axis. 0 = +Z; 1 = -X; 2 = -Z; 3 = +X;
	 * 
	 * @param player
	 * @return
	 */
	public static int getDirection(EntityPlayer player) {
		return MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	}
	
	/**
	 * Drop an ItemStack into the world
	 * 
	 * @param world
	 * @param itemStack
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void dropItemStack(World world, ItemStack itemStack, int x,
			int y, int z) {
		dropItemStack(world, itemStack, x, y, z, null);
	}
	
	/**
	 * Drop an ItemStack with an NBTTagCompound into the world
	 * 
	 * @param world
	 * @param itemStack
	 * @param x
	 * @param y
	 * @param z
	 * @param tagCom
	 */
	public static void dropItemStack(World world, ItemStack itemStack, int x,
			int y, int z, NBTTagCompound tagCom) {
		Random rand = new Random();
		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 0.8F + 0.1F;
		EntityItem entityitem = new EntityItem(world, (double) ((float) x + f),
				(double) ((float) y + f1), (double) ((float) z
						+ rand.nextFloat() * 0.8F + 0.1F), itemStack);
		float f3 = 0.05F;
		entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
		entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
		entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
		if (tagCom != null)
			entityitem.getEntityItem().setTagCompound(tagCom);
		if (!world.isRemote)
			world.spawnEntityInWorld(entityitem);
	}
	
	public static boolean chance(int percent) {
		return (new Random()).nextInt(100) < percent;
	}
	
}
