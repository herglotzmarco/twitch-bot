package de.herglotz.twitch.events;

import de.herglotz.twitch.messages.Message;

public class MessageEvent implements Event {

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
