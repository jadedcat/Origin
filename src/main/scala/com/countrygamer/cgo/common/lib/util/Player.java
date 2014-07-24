package com.countrygamer.cgo.common.lib.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class Player {

	public static void sendMessageToPlayer(EntityPlayer player, String message) {
		//if (player.worldObj.isRemote)
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	public static boolean breakBlockAsPlayer(World world, EntityPlayer player, int x, int y, int z,
			Block block) {
		if (player == null || world == null)
			return false;
		WorldClient worldclient = Minecraft.getMinecraft().theWorld;
		worldclient.playAuxSFX(2001, x, y, z,
				Block.getIdFromBlock(block) + (worldclient.getBlockMetadata(x, y, z) << 12));

		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockToAir(x, y, z);
		block.onBlockDestroyedByPlayer(world, x, y, z, meta);

		return true;
	}

	public static int getDirection(EntityPlayer player) {
		return MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	}

	@SuppressWarnings("rawtypes")
	public static EntityPlayerMP getPlayerByUUID(UUID id) {
		List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (Object player1 : players) {
			if (player1 instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) player1;
				if (player.getUniqueID().equals(id)) {
					return player;
				}
			}
		}
		return null;
	}

	public static String getUsername(EntityPlayer player) {
		return player.getCommandSenderName(); // Also player.getGameProfile().getName()
	}

	public static EntityPlayerMP getPlayerByUsername(String name) {
		List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (Object player1 : players) {
			if (player1 instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) player1;
				if (player.getCommandSenderName().equals(name)) {
					return player;
				}
			}
		}
		return null;
	}

}
