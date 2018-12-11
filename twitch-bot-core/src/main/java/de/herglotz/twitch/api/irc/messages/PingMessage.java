package de.herglotz.twitch.api.irc.messages;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.PingMessageEvent;

public class PingMessage extends Message {

	public PingMessage(String message) {
		super(message);
	}

	@Override
	public Event toEvent() {
		return new PingMessageEvent(this);
	}

	@Override
	public String toString() {
		return "PingMessage [" + getMessage() + "]";
	}

}
