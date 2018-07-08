package de.herglotz.twitch.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;

import de.herglotz.twitch.api.irc.TestableWriter;
import de.herglotz.twitch.api.irc.TwitchChatMessageFormatter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class HiCommandTest {

	@Test
	public void testHiMessage() throws Exception {
		HiCommand command = Mockito.spy(HiCommand.class);
		Mockito.when(command.pickRandomMessage()).thenReturn("Hello World");

		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		command.run(writer, new CommandMessage("user", "channel", "hi", new ArrayList<>()));
		assertEquals((TwitchChatMessageFormatter.format("channel", "Hello World") + System.lineSeparator()), writer.getText());
	}

	@Test
	public void testCheckForCorrectCommandText() throws Exception {
		HiCommand command = new HiCommand();
		assertTrue(command.isResponsible("hi"));
	}

	@Test
	public void testCheckForIncorrectCommandText() throws Exception {
		HiCommand command = new HiCommand();
		assertFalse(command.isResponsible("somethingElse"));
	}

	@Test
	public void testThatCommandDoesNotRunIfNotCorrectMessage() throws Exception {
		HiCommand command = new HiCommand();
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		command.run(writer, new CommandMessage("user", "channel", "!somethingElse", new ArrayList<>()));
		assertEquals("", writer.getText());
	}

}
