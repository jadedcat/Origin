package com.countrygamer.core.Base.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandler extends FMLIndexedMessageToMessageCodec<AbstractMessage> {
	
	private static final Map<String, PacketHandler> TRACKER = new HashMap<String, PacketHandler>();
	
	public static boolean registerHandler(String pluginID,
			Class<? extends AbstractMessage>... packetClasses) {
		if (!PacketHandler.TRACKER.containsKey(pluginID.toLowerCase())) {
			PacketHandler handler = new PacketHandler(pluginID, packetClasses);
			
			handler.channels = NetworkRegistry.INSTANCE.newChannel(pluginID.toLowerCase(), handler);
			
			PacketHandler.TRACKER.put(pluginID.toLowerCase(), handler);
			
			return true;
		}
		else {
			System.err.println("There is already a channel/handler for key/channel "
					+ pluginID.toLowerCase());
			return false;
		}
	}
	
	public static boolean sendToAll(String channel, AbstractMessage packet) {
		if (PacketHandler.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = PacketHandler.TRACKER.get(channel
					.toLowerCase()).channels;
			
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.ALL);
			channels.get(Side.SERVER).writeAndFlush(packet);
			
			return true;
		}
		return false;
	}
	
	public static boolean sendToPlayer(String channel, AbstractMessage packet, EntityPlayer player) {
		if (PacketHandler.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = PacketHandler.TRACKER.get(channel
					.toLowerCase()).channels;
			
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.PLAYER);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
			channels.get(Side.SERVER).writeAndFlush(packet);
			
			return true;
		}
		return false;
	}
	
	public static boolean sendToAllAround(String channel, AbstractMessage packet,
			NetworkRegistry.TargetPoint point) {
		if (PacketHandler.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = PacketHandler.TRACKER.get(channel
					.toLowerCase()).channels;
			
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
			channels.get(Side.SERVER).writeAndFlush(packet);
			
			return true;
		}
		return false;
	}
	
	public static boolean sendToDimension(String channel, AbstractMessage packet, int dimension) {
		if (PacketHandler.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = PacketHandler.TRACKER.get(channel
					.toLowerCase()).channels;
			
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.DIMENSION);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
			channels.get(Side.SERVER).writeAndFlush(packet);
			
			return true;
		}
		return false;
	}
	
	public static boolean sendToServer(String channel, AbstractMessage packet) {
		if (PacketHandler.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = PacketHandler.TRACKER.get(channel
					.toLowerCase()).channels;
			
			channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
			.set(FMLOutboundHandler.OutboundTarget.TOSERVER);
			channels.get(Side.CLIENT).writeAndFlush(packet);
			
			return true;
		}
		return false;
	}
	
	public final String channel;
	public EnumMap<Side, FMLEmbeddedChannel> channels;
	
	PacketHandler(String pluginID, Class<? extends AbstractMessage>... packetClasses) {
		this.channel = pluginID.toLowerCase();
		
		ArrayList<Class<? extends AbstractMessage>> list = new ArrayList<Class<? extends AbstractMessage>>();
		for (int i = 0; i < packetClasses.length; i++) {
			
			if (!list.contains(packetClasses[i])) {
				list.add(packetClasses[i]);
			}
			else {
				System.out.println("Channel " + channel
						+ " has already registered message/packet class "
						+ packetClasses[i].getSimpleName());
			}
			
			this.addDiscriminator(i, packetClasses[i]);
		}
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf target)
			throws Exception {
		try {
			// side = FMLCommonHandler.instance().getEffectiveSide()
			msg.writeTo_do(target);
		} catch (Exception e) {
			System.out.println("Error writing to packet for channel: " + channel);
			e.printStackTrace();
		}
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, AbstractMessage msg) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		EntityPlayer player = null;
		if (side.isServer()) {
			INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			player = ((NetHandlerPlayServer) netHandler).playerEntity;
		}
		else {
			player = this.getClientPlayer();
		}
		
		try {
			msg.readFrom_do(source, player, side);
		} catch (Exception e) {
			System.out.println("Error reading from packet for channel: " + channel);
			e.printStackTrace();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	
}
