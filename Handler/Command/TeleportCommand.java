package com.countrygamer.core.Handler.Command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.countrygamer.core.Core;
import com.countrygamer.core.lib.CoreUtil;

public class TeleportCommand implements ICommand {

	private List<String> aliases;

	public TeleportCommand() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("cgctp");
	}

	@Override
	public String getCommandName() {
		return "Core Teleport";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "cgctp <playername> <dimension name> <x> <y> <z>";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length == 0) {
			// TODO
			// icommandsender.sendChatToPlayer((new ChatMessageComponent())
			// .addText("Invalid Arguments"));
			return;
		}
		String playerName = astring[0];
		String dimensionName = astring[1];
		double x, y, z;
		try {
			x = Double.parseDouble(astring[2]);
			y = Double.parseDouble(astring[3]);
			z = Double.parseDouble(astring[4]);
		} catch (NumberFormatException e) {
			// TODO
			// icommandsender.sendChatToPlayer((new ChatMessageComponent())
			// .addText("Invalid Arguments"));
			if (Core.DEBUG) {
				Core.log.info("Cannot parse arguments");
				System.err.println(e.getStackTrace());
			}
			return;
		}
		EntityPlayerMP playerMP = MinecraftServer.getServer()
				.getConfigurationManager().getPlayerForUsername(playerName);
		// CG_Core.instance.dimLoad();
		if (playerMP != null && Core.dimensions.get(dimensionName) != null) {
			CoreUtil.teleportPlayerToDimension(playerMP,
					Core.dimensions.get(dimensionName));
			CoreUtil.teleportPlayer(playerMP, x, y, z, false, false);
		} else {
			// TODO
			// icommandsender.sendChatToPlayer((new ChatMessageComponent())
			// .addText("Player " + playerName + " is not availible."));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

}
