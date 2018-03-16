package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class HiCommand implements Command {

	protected static final String REPLY = "Hello World!";

	@Override
	public void run(TwitchChatWriter writer, CommandMessage commandMessage) {
		if (isResponsible(commandMessage.getCommand()))
			writer.sendChatMessage(commandMessage.getTargetChannel(), REPLY);
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return commandMessage.equals("!hi");
	}

}
