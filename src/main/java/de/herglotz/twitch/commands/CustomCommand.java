package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class CustomCommand {

	private String message;

	public CustomCommand(CustomCommandEntity entity) {
		message = entity.getMessage();
	}

	public void run(TwitchChatWriter writer, CommandMessage commandMessage, CustomCommandParser parser) {
		writer.sendChatMessage(commandMessage.getTargetChannel(), parser.parse(message, commandMessage));
	}

}
