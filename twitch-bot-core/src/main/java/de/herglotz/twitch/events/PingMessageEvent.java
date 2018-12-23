package de.herglotz.twitch.events;

import de.herglotz.twitch.messages.Message;

public class PingMessageEvent extends MessageEvent {

	public PingMessageEvent(Message message) {
		super(message);
	}

}
