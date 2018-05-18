package de.herglotz.twitch.events.listeners;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.commands.Command;

public class CommandHandlerTest {

	@Test
	public void testThatCommandGetsCalled() throws Exception {
		CommandHandler handler = new CommandHandler();
		TestCommand testCommand = new TestCommand("test");
		handler.register(testCommand);
		handler.handleEvent(new CommandMessage("user", "channel", "test", new ArrayList<>()).toEvent());
		assertTrue(testCommand.called);
	}

	@Test
	public void testThatCorrectCommandIsCalled() throws Exception {
		CommandHandler handler = new CommandHandler();
		TestCommand correctCommand = new TestCommand("test");
		TestCommand wrongCommand = new TestCommand("somethingElse");
		handler.register(correctCommand);
		handler.register(wrongCommand);

		handler.handleEvent(new CommandMessage("user", "channel", "test", new ArrayList<>()).toEvent());
		assertTrue(correctCommand.called);
		assertFalse(wrongCommand.called);
	}

	private class TestCommand implements Command {

		private boolean called;
		private String command;

		public TestCommand(String command) {
			this.command = command;
			called = false;
		}

		@Override
		public void run(TwitchChatWriter writer, CommandMessage commandMessage) {
			called = true;
		}

		@Override
		public boolean isResponsible(String commandMessage) {
			return commandMessage.equals(command);
		}

	}

}
