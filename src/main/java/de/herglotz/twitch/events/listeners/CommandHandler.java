package de.herglotz.twitch.events.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Sets;

import de.herglotz.twitch.api.irc.TwitchApi;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.commands.CounterCommand;
import de.herglotz.twitch.commands.CustomCommands;
import de.herglotz.twitch.commands.HiCommand;
import de.herglotz.twitch.events.CommandMessageEvent;
import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.persistence.Database;
import de.herglotz.twitch.persistence.entities.CustomCommandEntity;
import de.herglotz.twitch.persistence.entities.TimedCommandEntity;

public class CommandHandler implements EventListener {

	private static final int MAX_DELAY = 300000;
	private static final Object MUTEX = new Object();

	private Set<Command> commands;
	private Set<TimedCommandEntity> timedCommands;
	private Database database;
	private int maxDelay;

	public CommandHandler(Database database) {
		this(database, MAX_DELAY);
	}

	public CommandHandler(Database database, int maxDelay) {
		this.database = database;
		this.maxDelay = maxDelay;
		commands = new HashSet<>();
		register(new HiCommand());
		register(new CustomCommands(database.findAll(CustomCommandEntity.class)));
		register(new CounterCommand(database));

		timedCommands = Sets.newHashSet(database.findAll(TimedCommandEntity.class));
		startTimedCommands();
	}

	private void startTimedCommands() {
		for (TimedCommandEntity command : getTimedCommands()) {
			createTimer(command);
		}
	}

	private void createTimer(TimedCommandEntity command) {
		Timer timer = new Timer(String.format("%s_%s_Timer", command.getCommand(), command.getTimeInSeconds()), true);
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				handleEvent(new CommandMessageEvent(
						new CommandMessage("", command.getTargetChannel(), command.getCommand(), new ArrayList<>())));
				if (!getTimedCommands().contains(command))
					timer.cancel();
			}
		}, new Random().nextInt(maxDelay), command.getTimeInSeconds() * 1000L);
	}

	private Set<TimedCommandEntity> getTimedCommands() {
		Set<TimedCommandEntity> copy = new HashSet<>();
		synchronized (MUTEX) {
			copy.addAll(timedCommands);
		}
		return copy;
	}

	public void addTimedCommand(String command, String targetChannel, int periodInSeconds) {
		TimedCommandEntity entity = new TimedCommandEntity();
		entity.setCommand(command);
		entity.setTargetChannel(targetChannel);
		entity.setTimeInSeconds(periodInSeconds);
		database.persist(entity);
		synchronized (MUTEX) {
			timedCommands.add(entity);
		}
		createTimer(entity);
	}

	public void removeTimedCommand(String command) {
		synchronized (MUTEX) {
			timedCommands.stream().filter(c -> c.getCommand().equals(command)).findFirst()//
					.ifPresent(c -> {
						database.delete(c);
						timedCommands.remove(c);
					});
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(TwitchApi.instance().getWriter(), ((CommandMessageEvent) event).getMessage()));
		}
	}

	public void register(Command command) {
		commands.add(command);
	}

}
