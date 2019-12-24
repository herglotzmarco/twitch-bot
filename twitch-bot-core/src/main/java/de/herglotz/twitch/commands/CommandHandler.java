package de.herglotz.twitch.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.common.collect.Sets;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.commands.custom.CustomCommands;
import de.herglotz.twitch.events.CommandMessageEvent;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.CommandMessage;
import de.herglotz.twitch.persistence.Database;

@ApplicationScoped
public class CommandHandler {

	private static final int MAX_DELAY = 1200000;
	private static final Object MUTEX = new Object();

	@Inject
	private Database database;

	@Inject
	private TwitchChat twitch;

	private Set<Command> commands;
	private Set<TimedCommandEntity> timedCommands;
	private int maxDelay;

	public CommandHandler() {
		this(MAX_DELAY);
	}

	public CommandHandler(int maxDelay) {
		this.maxDelay = maxDelay;
	}

	@PostConstruct
	public void init() {
		commands = new HashSet<>();
		register(new HiCommand());
		register(new CustomCommands(database.findAll(CustomCommandEntity.class)));

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
				handleEvent(new CommandMessageEvent(new CommandMessage("", command.getCommand(), new ArrayList<>())));
				if (!getTimedCommands().contains(command)) {
					timer.cancel();
				}
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

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(twitch, ((CommandMessageEvent) event).getMessage()));
		}
	}

	public void register(Command command) {
		commands.add(command);
	}

}
