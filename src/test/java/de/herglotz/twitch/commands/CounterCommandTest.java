package de.herglotz.twitch.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.herglotz.twitch.api.irc.TestableWriter;
import de.herglotz.twitch.api.irc.TwitchChatMessageFormatter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.persistence.Database;
import de.herglotz.twitch.persistence.entities.CounterCommandEntity;

public class CounterCommandTest {

	@Test
	public void testGetCommandWorks() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(1));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		assertTrue(underTest.isResponsible("deathctr"));
		underTest.run(writer, new CommandMessage("", "target", "deathctr", new ArrayList<>()));
		assertEquals(
				TwitchChatMessageFormatter.format("target", "Der Counter steht aktuell bei 1" + System.lineSeparator()),
				writer.getText());
	}

	@Test
	public void testGetCommandIfNoDatabaseEntry() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(-1));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer, new CommandMessage("", "target", "deathctr", new ArrayList<>()));
		assertEquals(
				TwitchChatMessageFormatter.format("target", "Der Counter steht aktuell bei 0" + System.lineSeparator()),
				writer.getText());
	}

	@Test
	public void testGetCommandIfMoreText() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(4));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer,
				new CommandMessage("", "target", "deathctr", Lists.newArrayList("some", "thing", "useless")));
		assertEquals(
				TwitchChatMessageFormatter.format("target", "Der Counter steht aktuell bei 4" + System.lineSeparator()),
				writer.getText());
	}

	@Test
	public void testAddCommand() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(2));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer, new CommandMessage("", "target", "deathctr", Lists.newArrayList("add")));
		assertEquals(TwitchChatMessageFormatter.format("target",
				"Der Counter wurde erhöht und steht jetzt bei 3" + System.lineSeparator()), writer.getText());
	}

	@Test
	public void testAddCommandIfNoDatabaseEntry() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(-1));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer, new CommandMessage("", "target", "deathctr", Lists.newArrayList("+")));
		assertEquals(TwitchChatMessageFormatter.format("target",
				"Der Counter wurde erhöht und steht jetzt bei 1" + System.lineSeparator()), writer.getText());
	}

	@Test
	public void testSubCommand() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(2));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer, new CommandMessage("", "target", "deathctr", Lists.newArrayList("sub")));
		assertEquals(
				TwitchChatMessageFormatter.format("target",
						"Der Counter wurde erniedrigt und steht jetzt bei 1" + System.lineSeparator()),
				writer.getText());
	}

	@Test
	public void testSubCommandIfNoDatabaseEntry() throws Exception {
		CounterCommand underTest = new CounterCommand(new CounterDatabase(-1));
		TestableWriter writer = new TestableWriter(new ByteArrayOutputStream());
		underTest.run(writer, new CommandMessage("", "target", "deathctr", Lists.newArrayList("-")));
		assertEquals(
				TwitchChatMessageFormatter.format("target", "Der Counter steht bereits bei 0" + System.lineSeparator()),
				writer.getText());
	}

	private class CounterDatabase implements Database {
		private int i;

		public CounterDatabase(int i) {
			this.i = i;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> List<T> findAll(Class<T> clazz) {
			ArrayList<CounterCommandEntity> list = Lists.newArrayList();
			if (i != -1) {
				CounterCommandEntity counter = new CounterCommandEntity();
				counter.setCommand("deathctr");
				counter.setCounter(i);
				list.add(counter);
			}
			return (List<T>) list;
		}

		@Override
		public void persist(Object entity) {
		}

		@Override
		public void delete(Object entity) {
		}
	}

}
