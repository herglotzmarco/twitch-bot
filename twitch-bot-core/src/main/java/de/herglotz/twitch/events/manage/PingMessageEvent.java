package de.herglotz.twitch.events.manage;

import de.herglotz.twitch.events.message.MessageEvent;
import de.herglotz.twitch.messages.Message;

public class PingMessageEvent extends MessageEvent {

	public PingMessageEvent(Message message) {
		super(message);
	}

}
