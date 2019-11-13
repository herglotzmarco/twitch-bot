package de.herglotz.twitch.commands.custom;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.messages.CommandMessage;

public class CustomCommand {

	private String message;

	public CustomCommand(CustomCommandEntity entity) {
		message = entity.getMessage();
	}

	public void run(TwitchChat twitch, CommandMessage commandMessage) {
		twitch.sendChatMessage(new CustomCommandParser().parse(message, commandMessage));
	}

}
