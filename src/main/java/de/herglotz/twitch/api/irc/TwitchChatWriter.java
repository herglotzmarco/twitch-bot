package de.herglotz.twitch.api.irc;

import java.io.PrintWriter;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.EventListener;
import de.herglotz.twitch.events.PingMessageEvent;

public class TwitchChatWriter implements EventListener {

	protected static final String TWITCH_API_MSG = "PRIVMSG #%s :%s";
	protected static final String TWITCH_API_PONG = "PONG :tmi.twitch.tv";

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
		writer.println(String.format(TWITCH_API_MSG, targetChannel, message));
		writer.flush();
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof PingMessageEvent) {
			sendRawMessage(TWITCH_API_PONG);
		}
	}

}
