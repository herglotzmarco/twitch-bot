package de.herglotz.twitch.commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import de.herglotz.twitch.api.irc.TwitchChatWriter;

public class TestableWriter extends TwitchChatWriter {

	private ByteArrayOutputStream output;

	public TestableWriter(ByteArrayOutputStream output) {
		super(new PrintWriter(output));
		this.output = output;
	}

	public String getText() {
		return new String(output.toByteArray(), Charset.forName("UTF-8"));
	}

}
