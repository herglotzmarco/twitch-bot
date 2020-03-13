package de.herglotz.twitch.messages;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.message.ChatMessageEvent;

public class ChatMessage extends Message {

	private String username;

	public ChatMessage(String username, String message) {
		super(message);
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public TwitchEvent toEvent() {
		return new ChatMessageEvent(this);
	}

	@Override
	public String toString() {
		return "ChatMessage [username=" + username + ", message=" + getMessage() + "]";
	}
}
