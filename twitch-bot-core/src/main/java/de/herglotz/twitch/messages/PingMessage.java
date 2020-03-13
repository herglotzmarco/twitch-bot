package de.herglotz.twitch.messages;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.manage.PingMessageEvent;

public class PingMessage extends Message {

	public PingMessage(String message) {
		super(message);
	}

	@Override
	public TwitchEvent toEvent() {
		return new PingMessageEvent(this);
	}

	@Override
	public String toString() {
		return "PingMessage [" + getMessage() + "]";
	}

}
