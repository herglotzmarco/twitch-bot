package de.herglotz.twitch.messages;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.MessageEvent;

public class Message {

	private String message;

	public Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public TwitchEvent toEvent() {
		return new MessageEvent(this);
	}

	@Override
	public String toString() {
		return "Message [message=" + message + "]";
	}

}
