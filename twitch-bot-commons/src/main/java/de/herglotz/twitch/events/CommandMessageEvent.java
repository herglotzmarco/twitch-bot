package de.herglotz.twitch.events;

import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class CommandMessageEvent extends ChatMessageEvent {

	public CommandMessageEvent(CommandMessage chatMessage) {
		super(chatMessage);
	}

	@Override
	public CommandMessage getMessage() {
		return (CommandMessage) super.getMessage();
	}

}
