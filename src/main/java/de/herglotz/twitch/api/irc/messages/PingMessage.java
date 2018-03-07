package de.herglotz.twitch.api.irc.messages;

public class PingMessage extends Message {

	public PingMessage(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "PingMessage [" + getMessage() + "]";
	}

}
