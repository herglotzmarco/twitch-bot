package de.herglotz.twitch.api.irc;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class TestableWriter implements ITwitchChatWriter {

	private ByteArrayOutputStream output;
	private PrintWriter writer;

	public TestableWriter(ByteArrayOutputStream output) {
		this.writer = new PrintWriter(output);
		this.output = output;
	}

	public String getText() {
		return new String(output.toByteArray(), Charset.forName("UTF-8"));
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
