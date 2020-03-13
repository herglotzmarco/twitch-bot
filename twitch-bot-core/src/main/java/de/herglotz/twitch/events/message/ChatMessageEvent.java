package de.herglotz.twitch.events.message;

import de.herglotz.twitch.messages.ChatMessage;

public class ChatMessageEvent extends MessageEvent {

	public ChatMessageEvent(ChatMessage chatMessage) {
		super(chatMessage);
	}

	@Override
	public ChatMessage getMessage() {
		return (ChatMessage) super.getMessage();
	}

}
