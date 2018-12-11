package de.herglotz.twitch.api.irc;

import java.io.PrintWriter;

public class TwitchChatWriter implements ITwitchChatWriter {

	private PrintWriter writer;

	public TwitchChatWriter(PrintWriter writer) {
		this.writer = writer;
	}

	@Override
	public void sendRawMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	@Override
	public void sendChatMessage(String targetChannel, String message) {
		writer.println(TwitchChatMessageFormatter.format(targetChannel, message));
		writer.flush();
	}

}
