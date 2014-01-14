package CountryGamer_Core.Handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import CountryGamer_Core.lib.CoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class CorePacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals("CGC_Teleport")) {
			this.handleTeleport(packet, (EntityPlayer) player);
		}
	}

	private void handleTeleport(Packet250CustomPayload packet,
			EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));
		int dimID;
		double x, y, z;
		try {
			dimID = inputStream.readInt();
			x = inputStream.readDouble();
			y = inputStream.readDouble();
			z = inputStream.readDouble();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		CoreUtil.teleportPlayerToDimension(player, dimID);
		CoreUtil.teleportPlayer(player, x, y, z, true, true);
	}

}
