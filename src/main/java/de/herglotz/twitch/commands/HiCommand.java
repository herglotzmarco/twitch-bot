package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class HiCommand {

	protected static final String REPLY = "Hello World!";

	public void run(TwitchChatWriter writer, CommandMessage commandMessage) {
		if (isResponsible(commandMessage.getCommand()))
			writer.sendChatMessage(commandMessage.getTargetChannel(), REPLY);
	}

	public boolean isResponsible(String commandMessage) {
		return commandMessage.equals("!hi");
	}

}
