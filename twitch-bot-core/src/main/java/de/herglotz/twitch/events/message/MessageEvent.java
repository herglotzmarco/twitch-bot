package de.herglotz.twitch.events.message;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.Message;

public class MessageEvent implements TwitchEvent {

	private Message message;

	public MessageEvent(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message.toString();
	}

}
