package de.herglotz.twitch.commands.counter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.persistence.Database;
import de.herglotz.twitch.persistence.entities.CounterCommandEntity;

public class CounterCommand implements Command {

	private static final String GET_MESSAGE = "Der Counter steht aktuell bei %s";

	private String prefix;

	private Database database;

	public CounterCommand(Database database) {
		this.database = database;
		this.prefix = "death";
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			setupKeylistener();
		}
	}

	@Override
	public void run(ITwitchChatWriter writer, CommandMessage commandMessage) {
		writer.sendChatMessage(commandMessage.getTargetChannel(), String.format(GET_MESSAGE, findValue()));
	}

	private void setupKeylistener() {
		try {
			GlobalScreen.registerNativeHook();
			LogManager.getLogManager().getLogger("org.jnativehook").setLevel(Level.OFF);
			GlobalScreen.addNativeKeyListener(new CounterKeyListener(this::runAddCommand, this::runSubCommand));
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
	}

	private void runAddCommand() {
		CounterCommandEntity entity = findOrCreateEntity();
		entity.increaseCounter();
		database.persist(entity);
		updateFile(entity.getCounter());
	}

	private void runSubCommand() {
		CounterCommandEntity entity = findOrCreateEntity();
		if (entity.getCounter() > 0) {
			entity.decreaseCounter();
			database.persist(entity);
			updateFile(entity.getCounter());
		}
	}

	private void updateFile(int counter) {
		try (FileWriter writer = new FileWriter("D:\\Videos\\Stream\\Bot Counter\\" + prefix + "ctr.txt", false)) {
			writer.write("" + counter);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
