package de.herglotz.twitch.api.irc.messages;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.MessageEvent;

public class RawMessage extends Message {

	public RawMessage(String message) {
		super(message);
	}

	@Override
	public Event toEvent() {
		return new MessageEvent(this);
	}

	@Override
	public String toString() {
		return "RawMessage [" + getMessage() + "]";
	}

}
