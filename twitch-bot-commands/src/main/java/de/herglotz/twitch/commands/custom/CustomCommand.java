package de.herglotz.twitch.commands.custom;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class CustomCommand {

	private String message;

	public CustomCommand(CustomCommandEntity entity) {
		message = entity.getMessage();
	}

	public void run(ITwitchChatWriter writer, CommandMessage commandMessage) {
		writer.sendChatMessage(commandMessage.getTargetChannel(),
				new CustomCommandParser().parse(message, commandMessage));
	}

}
