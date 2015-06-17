package com.temportalist.origin.internal.common.network.handler;

import com.temportalist.origin.foundation.common.network.IPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.*;

/**
 * @author TheTemportalist
 */
public class Network extends FMLIndexedMessageToMessageCodec<IPacket> {

	private static final Map<String, Network> TRACKER = new HashMap<String, Network>();
	private static final Map<Class<? extends IPacket>, String> packetToChannel =
			new HashMap<Class<? extends IPacket>, String>();

	@SuppressWarnings("unchecked")
	public static boolean registerHandler(String modid,
			List<Class<? extends IPacket>> packetClasses) {
		return Network.registerHandler(modid, packetClasses.toArray(new Class[0]));
	}

	public static boolean registerHandler(String modid,
			Class<? extends IPacket>... packetClasses) {
		if (!Network.TRACKER.containsKey(modid.toLowerCase())) {
			Network handler = new Network(modid, packetClasses);
			for (Class<? extends IPacket> packet : packetClasses) {
				Network.packetToChannel.put(packet, modid);
			}

			/*
			Origin.log("\tRegistered Packets:");
			for (Class<? extends IPacket> packet : Network.packetToChannel.keySet()) {
				Origin.log("\t\t" + packet.getCanonicalName());
			}
			*/

			handler.channels = NetworkRegistry.INSTANCE.newChannel(modid.toLowerCase(), handler);

			PacketExecuter executer = new PacketExecuter();

			for (Map.Entry<Side, FMLEmbeddedChannel> e : handler.channels.entrySet()) {
				FMLEmbeddedChannel channel = e.getValue();
				String codec = channel.findChannelHandlerNameForType(Network.class);
				channel.pipeline().addAfter(codec, "PacketExecuter", executer);
			}

			Network.TRACKER.put(modid.toLowerCase(), handler);

			return true;
		}
		else {
			throw new UnsupportedOperationException(
					"There is already a channel/handler for key/channel " + modid.toLowerCase());
		}
	}

	public static boolean sendToClients(IPacket packet) {
		String channel = Network.getChannel(packet.getClass());
		if (Network.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = Network.TRACKER.get(channel
					.toLowerCase()).channels;

			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
					.set(FMLOutboundHandler.OutboundTarget.ALL);
			channels.get(Side.SERVER).writeAndFlush(packet);

			return true;
		}
		return false;
	}

	public static boolean sendToPlayer(IPacket packet, EntityPlayer player) {
		String channel = Network.getChannel(packet.getClass());
		if (Network.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = Network.TRACKER.get(channel
					.toLowerCase()).channels;

			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
					.set(FMLOutboundHandler.OutboundTarget.PLAYER);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
			channels.get(Side.SERVER).writeAndFlush(packet);

			return true;
		}
		return false;
	}

	public static boolean sendToAllAround(IPacket packet, NetworkRegistry.TargetPoint point) {
		String channel = Network.getChannel(packet.getClass());
		if (Network.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = Network.TRACKER.get(channel
					.toLowerCase()).channels;

			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
					.set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
			channels.get(Side.SERVER).writeAndFlush(packet);

			return true;
		}
		return false;
	}

	public static boolean sendToDimension(IPacket packet, int dimension) {
		String channel = Network.getChannel(packet.getClass());
		if (Network.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = Network.TRACKER.get(channel
					.toLowerCase()).channels;

			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
					.set(FMLOutboundHandler.OutboundTarget.DIMENSION);
			channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
			channels.get(Side.SERVER).writeAndFlush(packet);

			return true;
		}
		return false;
	}

	public static boolean sendToServer(IPacket packet) {
		String channel = Network.getChannel(packet.getClass());
		if (Network.TRACKER.containsKey(channel.toLowerCase())) {
			EnumMap<Side, FMLEmbeddedChannel> channels = Network.TRACKER.get(channel
					.toLowerCase()).channels;

			channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
					.set(FMLOutboundHandler.OutboundTarget.TOSERVER);
			channels.get(Side.CLIENT).writeAndFlush(packet);

			return true;
		}
		return false;
	}

	public static void sendToServerAndClients(IPacket packet) {
		Network.sendToServer(packet);
		Network.sendToClients(packet);
	}

	public static String getChannel(Class<? extends IPacket> packet) {
		if (!Network.packetToChannel.containsKey(packet))
			throw new NoSuchElementException("Packet type " + packet.getCanonicalName()
					+ " is not registered! This is an issue!");
		return Network.packetToChannel.get(packet);
	}

	public final String channel;
	public EnumMap<Side, FMLEmbeddedChannel> channels;

	private Network(String pluginID, Class<? extends IPacket>... packetClasses) {
		this.channel = pluginID.toLowerCase();

		ArrayList<Class<? extends IPacket>> list = new ArrayList<Class<? extends IPacket>>();
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
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target)
			throws Exception {
		try {
			// side = FMLCommonHandler.instance().getEffectiveSide()
			msg.toBytes(target);
		} catch (Exception e) {
			System.out.println("Error writing to packet for channel: " + channel);
			e.printStackTrace();
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {

		try {
			msg.fromBytes(source);// (source, player, side);
		} catch (Exception e) {
			System.out.println("Error reading from packet for channel: " + channel);
			e.printStackTrace();
		}
	}

	// Execution of Packets
	@Sharable
	private static class PacketExecuter extends SimpleChannelInboundHandler<IPacket> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, IPacket msg)
				throws Exception {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			EntityPlayer player;
			if (side.isServer()) {
				INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
				player = ((NetHandlerPlayServer) netHandler).playerEntity;
			}
			else {
				player = this.getClientPlayer();
			}

			if (side.isClient())
				msg.handleOnClient(player);
			else if (side.isServer())
				msg.handleOnServer(player);

		}

		@SideOnly(Side.CLIENT)
		public EntityPlayer getClientPlayer() {
			return Minecraft.getMinecraft().thePlayer;
		}

	}

}
