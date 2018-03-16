package de.herglotz.twitch.commands;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.Test;

import de.herglotz.twitch.api.irc.TwitchChatMessageFormatter;
import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class HiCommandTest {

	@Test
	public void testHiMessage() throws Exception {
		HiCommand command = new HiCommand();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		command.run(new TwitchChatWriter(new PrintWriter(output)),
				new CommandMessage("user", "channel", "!hi", new ArrayList<>()));
		assertArrayEquals((TwitchChatMessageFormatter.format("channel", HiCommand.REPLY) + "\r\n")
				.getBytes(Charset.forName("UTF-8")), output.toByteArray());
	}

	@Test
	public void testCheckForCorrectCommandText() throws Exception {
		HiCommand command = new HiCommand();
		assertTrue(command.isResponsible("!hi"));
	}

	@Test
	public void testCheckForIncorrectCommandText() throws Exception {
		HiCommand command = new HiCommand();
		assertFalse(command.isResponsible("!somethingElse"));
	}

	@Test
	public void testThatCommandDoesNotRunIfNotCorrectMessage() throws Exception {
		HiCommand command = new HiCommand();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		command.run(new TwitchChatWriter(new PrintWriter(output)),
				new CommandMessage("user", "channel", "!somethingElse", new ArrayList<>()));
		assertArrayEquals("".getBytes(Charset.forName("UTF-8")), output.toByteArray());
	}

}
