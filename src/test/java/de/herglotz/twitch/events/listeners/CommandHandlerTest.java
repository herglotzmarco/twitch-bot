package de.herglotz.twitch.events.listeners;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.persistence.TestableDatabase;
import de.herglotz.twitch.persistence.entities.TimedCommandEntity;

public class CommandHandlerTest {

	@Test
	public void testThatCommandGetsCalled() throws Exception {
		CommandHandler handler = new CommandHandler(new TestableDatabase(), 1000);
		TestCommand testCommand = new TestCommand("test");
		handler.register(testCommand);
		handler.handleEvent(new CommandMessage("user", "channel", "test", new ArrayList<>()).toEvent());
		assertTrue(testCommand.called);
	}

	@Test
	public void testThatCorrectCommandIsCalled() throws Exception {
		CommandHandler handler = new CommandHandler(new TestableDatabase(), 1000);
		TestCommand correctCommand = new TestCommand("test");
		TestCommand wrongCommand = new TestCommand("somethingElse");
		handler.register(correctCommand);
		handler.register(wrongCommand);

		handler.handleEvent(new CommandMessage("user", "channel", "test", new ArrayList<>()).toEvent());
		assertTrue(correctCommand.called);
		assertFalse(wrongCommand.called);
	}

	@Test
	public void testTimedCommandGetsCalled() throws Exception {
		TestableDatabase database = new TestableDatabase();
		TimedCommandEntity entity = new TimedCommandEntity();
		entity.setCommand("test");
		entity.setTargetChannel("channel");
		entity.setTimeInSeconds(1);
		database.persist(entity);

		CommandHandler handler = new CommandHandler(database, 1000);
		TestCommand correctCommand = new TestCommand("test");
		handler.register(correctCommand);

		Thread.sleep(2400); // wait an extra cycle because commands gets registered after timer started so
							// we might miss one
		assertTrue(correctCommand.called);
		correctCommand.called = false;
		assertFalse(correctCommand.called);
		Thread.sleep(1200);
		assertTrue(correctCommand.called);
	}

	@Test
	public void testAddTimedCommand() throws Exception {
		CommandHandler handler = new CommandHandler(new TestableDatabase(), 1000);
		TestCommand correctCommand = new TestCommand("test");
		handler.register(correctCommand);

		Thread.sleep(1200);
		assertFalse(correctCommand.called);
		handler.addTimedCommand("test", "channel", 1);
		assertFalse(correctCommand.called);
		Thread.sleep(1200);
		assertTrue(correctCommand.called);
	}

	@Test
	public void testRemoveCommand() throws Exception {
		CommandHandler handler = new CommandHandler(new TestableDatabase(), 1000);
		TestCommand correctCommand = new TestCommand("test");
		handler.register(correctCommand);

		handler.addTimedCommand("test", "channel", 1);
		Thread.sleep(1200);
		assertTrue(correctCommand.called);
		handler.removeTimedCommand("test");
		correctCommand.called = false;
		assertFalse(correctCommand.called);
		Thread.sleep(1200);
		assertTrue(correctCommand.called);
		correctCommand.called = false;
		assertFalse(correctCommand.called);
		Thread.sleep(1200);
		assertFalse(correctCommand.called);
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
