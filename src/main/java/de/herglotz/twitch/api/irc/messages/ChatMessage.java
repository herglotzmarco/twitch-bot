package de.herglotz.twitch.api.irc.messages;

import de.herglotz.twitch.events.ChatMessageEvent;
import de.herglotz.twitch.events.Event;

public class ChatMessage extends Message {

	private String username;
	private String targetChannel;

	public ChatMessage(String username, String targetChannel, String message) {
		super(message);
		this.username = username;
		this.targetChannel = targetChannel;
	}

	public String getUsername() {
		return this.username;
	}

	public String getTargetChannel() {
		return this.targetChannel;
	}

	@Override
	public Event toEvent() {
		return new ChatMessageEvent(this);
	}

	@Override
	public String toString() {
		return "ChatMessage [username=" + username + ", targetChannel=" + targetChannel + ", message=" + getMessage()
				+ "]";
	}
}
