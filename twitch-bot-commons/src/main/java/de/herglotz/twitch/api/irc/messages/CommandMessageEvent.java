package de.herglotz.twitch.api.irc.messages;

import de.herglotz.twitch.events.ChatMessageEvent;

public class CommandMessageEvent extends ChatMessageEvent {

	public CommandMessageEvent(CommandMessage chatMessage) {
		super(chatMessage);
	}

	@Override
	public CommandMessage getMessage() {
		return (CommandMessage) super.getMessage();
	}

}
