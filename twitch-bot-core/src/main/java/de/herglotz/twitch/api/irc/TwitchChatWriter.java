package de.herglotz.twitch.api.irc;

import java.io.PrintWriter;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.PingMessageEvent;
import de.herglotz.twitch.events.listeners.EventListener;

public class TwitchChatWriter implements EventListener {

	private PrintWriter writer;

	public TwitchChatWriter(PrintWriter writer) {
		this.writer = writer;
		EventBus.instance().register(this);
	}

	public void sendRawMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	public void sendChatMessage(String targetChannel, String message) {
		writer.println(TwitchChatMessageFormatter.format(targetChannel, message));
		writer.flush();
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof PingMessageEvent) {
			sendRawMessage(TwitchConstants.TWITCH_API_PONG);
		}
	}

}
