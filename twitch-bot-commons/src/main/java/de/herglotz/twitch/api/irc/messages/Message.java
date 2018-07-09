package de.herglotz.twitch.api.irc.messages;

import de.herglotz.twitch.events.Event;

public abstract class Message {

	private String message;

	public Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public abstract Event toEvent();

}
