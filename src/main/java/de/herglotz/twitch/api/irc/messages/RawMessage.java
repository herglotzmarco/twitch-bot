package de.herglotz.twitch.api.irc.messages;

public class RawMessage extends Message {

	public RawMessage(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "RawMessage [" + getMessage() + "]";
	}

}
