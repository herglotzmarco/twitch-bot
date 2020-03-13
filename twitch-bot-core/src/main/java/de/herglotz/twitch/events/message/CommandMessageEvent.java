package de.herglotz.twitch.events.message;

import de.herglotz.twitch.messages.CommandMessage;

public class CommandMessageEvent extends ChatMessageEvent {

	public CommandMessageEvent(CommandMessage chatMessage) {
		super(chatMessage);
	}

	@Override
	public CommandMessage getMessage() {
		return (CommandMessage) super.getMessage();
	}

}
