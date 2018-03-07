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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((targetChannel == null) ? 0 : targetChannel.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatMessage other = (ChatMessage) obj;
		if (targetChannel == null) {
			if (other.targetChannel != null)
				return false;
		} else if (!targetChannel.equals(other.targetChannel))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatMessage [username=" + username + ", targetChannel=" + targetChannel + ", message=" + getMessage()
				+ "]";
	}
}
