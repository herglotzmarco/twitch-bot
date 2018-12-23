package de.herglotz.twitch.messages;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.MessageEvent;

public class Message {

	private String message;

	public Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Event toEvent() {
		return new MessageEvent(this);
	}

	@Override
	public String toString() {
		return "Message [message=" + message + "]";
	}

}
