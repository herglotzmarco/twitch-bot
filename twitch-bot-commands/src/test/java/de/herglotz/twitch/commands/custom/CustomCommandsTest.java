package de.herglotz.twitch.commands.custom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.herglotz.twitch.api.irc.TestableWriter;
import de.herglotz.twitch.api.irc.TwitchChatMessageFormatter;
import de.herglotz.twitch.messages.CommandMessage;
import de.herglotz.twitch.commands.custom.CustomCommands;
import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class CustomCommandsTest {

	private CustomCommandEntity discordCommand;
	private CustomCommandEntity otherCommand;

	@Before
	public void init() {
		discordCommand = new CustomCommandEntity();
		discordCommand.setCommand("discord");
		discordCommand.setMessage("discord was called!");

		otherCommand = new CustomCommandEntity();
		otherCommand.setCommand("other");
		otherCommand.setMessage("other was called!");
	}

	@Test
	public void testThatCorrectCommandsAreResponsible() throws Exception {
		assertTrue(new CustomCommands(Sets.newHashSet(discordCommand, otherCommand)).isResponsible("discord"));
		assertTrue(new CustomCommands(Sets.newHashSet(discordCommand, otherCommand)).isResponsible("other"));
	}

	@Test
	public void testThatWrongCommandsAreNotResponsible() throws Exception {
		assertFalse(new CustomCommands(Sets.newHashSet(discordCommand, otherCommand)).isResponsible("something"));
	}

	@Test
	public void testThatCorrectCommandGetsCalled() throws Exception {
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		CustomCommands commands = new CustomCommands(Sets.newHashSet(discordCommand, otherCommand));
		commands.run(writer, new CommandMessage("user", "target", "discord", Lists.newArrayList("pls")));
		assertEquals(TwitchChatMessageFormatter.format("target", "discord was called!" + System.lineSeparator()),
				writer.getText());

		writer = new TestableWriter(new ByteArrayOutputStream());
		commands.run(writer, new CommandMessage("user", "target", "other",
				Lists.newArrayList("something else that does not affect the command")));
		assertEquals(TwitchChatMessageFormatter.format("target", "other was called!" + System.lineSeparator()),
				writer.getText());

		writer = new TestableWriter(new ByteArrayOutputStream());
		commands.run(writer, new CommandMessage("user", "target", "something", Lists.newArrayList("")));
		assertEquals("", writer.getText());
	}

	@Test
	public void testCommandParserIsUsed() throws Exception {
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		discordCommand.setMessage("@<user>: Discord was called!");
		CustomCommands commands = new CustomCommands(Sets.newHashSet(discordCommand));
		commands.run(writer, new CommandMessage("username", "target", "discord", Lists.newArrayList("pls")));
		assertEquals(
				TwitchChatMessageFormatter.format("target", "@username: Discord was called!" + System.lineSeparator()),
				writer.getText());
	}

}
