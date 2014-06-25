package com.countrygamer.countrygamercore.common.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import com.countrygamer.countrygamercore.common.Core;
import com.countrygamer.countrygamercore.common.lib.util.Player;
import com.countrygamer.countrygamercore.common.lib.util.UtilVector;

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
				Core.logger.info("Cannot parse arguments");
				System.err.println(e.getStackTrace());
			}
			return;
		}
		EntityPlayerMP playerMP = Player.getPlayerByUUID(UUID.fromString(playerName));
		// CG_Core.instance.dimLoad();
		if (playerMP != null && Core.dimensions.get(dimensionName) != null) {
			UtilVector.teleportPlayerToDimension(playerMP,
					Core.dimensions.get(dimensionName));
			UtilVector.teleportPlayer(playerMP, x, y, z, false, false);
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
