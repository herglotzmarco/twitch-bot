package de.herglotz.twitch.events;

import de.herglotz.twitch.messages.ChatMessage;

public class ChatMessageEvent extends MessageEvent {

	public ChatMessageEvent(ChatMessage chatMessage) {
		super(chatMessage);
	}

}
