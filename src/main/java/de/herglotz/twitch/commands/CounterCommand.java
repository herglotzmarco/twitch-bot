package de.herglotz.twitch.commands;

import java.util.List;
import java.util.Optional;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.persistence.Database;
import de.herglotz.twitch.persistence.entities.CounterCommandEntity;

public class CounterCommand implements Command {

	private static final String NO_REDUCE_MESSAGE = "Der Counter steht bereits bei 0";
	private static final String GET_MESSAGE = "Der Counter steht aktuell bei %s";
	private static final String SUB_MESSAGE = "Der Counter wurde erniedrigt und steht jetzt bei %s";
	private static final String ADD_MESSAGE = "Der Counter wurde erhöht und steht jetzt bei %s";

	private String prefix;

	private Database database;

	public CounterCommand(Database database) {
		this.database = database;
		this.prefix = "death";
	}

	@Override
	public void run(TwitchChatWriter writer, CommandMessage commandMessage) {
		if (commandMessage.getParameters().isEmpty()) {
			runGetCommand(writer, commandMessage);
		} else if (commandMessage.getParameters().get(0).equals("+")
				|| commandMessage.getParameters().get(0).equals("add")) {
			runAddCommand(writer, commandMessage);
		} else if (commandMessage.getParameters().get(0).equals("-")
				|| commandMessage.getParameters().get(0).equals("sub")) {
			runSubCommand(writer, commandMessage);
		} else {
			runGetCommand(writer, commandMessage);
		}
	}

	private void runAddCommand(TwitchChatWriter writer, CommandMessage commandMessage) {
		CounterCommandEntity entity = findOrCreateEntity();
		entity.increaseCounter();
		database.persist(entity);
		writer.sendChatMessage(commandMessage.getTargetChannel(), String.format(ADD_MESSAGE, entity.getCounter()));
	}

	private void runSubCommand(TwitchChatWriter writer, CommandMessage commandMessage) {
		CounterCommandEntity entity = findOrCreateEntity();
		if (entity.getCounter() == 0) {
			writer.sendChatMessage(commandMessage.getTargetChannel(), NO_REDUCE_MESSAGE);
		} else {
			entity.decreaseCounter();
			database.persist(entity);
			writer.sendChatMessage(commandMessage.getTargetChannel(), String.format(SUB_MESSAGE, entity.getCounter()));
		}
	}

	private void runGetCommand(TwitchChatWriter writer, CommandMessage commandMessage) {
		writer.sendChatMessage(commandMessage.getTargetChannel(), String.format(GET_MESSAGE, findValue()));
	}

	private int findValue() {
		return findOrCreateEntity().getCounter();
	}

	private CounterCommandEntity findOrCreateEntity() {
		List<CounterCommandEntity> list = database.findAll(CounterCommandEntity.class);
		Optional<CounterCommandEntity> entityOptional = list.stream()//
				.filter(c -> c.getCommand().equals(prefix + "ctr"))//
				.findFirst();
		if (!entityOptional.isPresent()) {
			CounterCommandEntity entity = new CounterCommandEntity();
			entity.setCommand(prefix + "ctr");
			entity.setCounter(0);
			database.persist(entity);
			entityOptional = Optional.of(entity);
		}
		return entityOptional.get();
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return (prefix + "ctr").equals(commandMessage);
	}

}
