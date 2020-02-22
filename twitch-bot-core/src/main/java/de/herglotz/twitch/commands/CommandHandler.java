package de.herglotz.twitch.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.custom.CustomCommand;
import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.CommandMessageEvent;
import de.herglotz.twitch.events.CommandsChangedEvent;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.CommandMessage;

@ApplicationScoped
public class CommandHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CommandHandler.class);
	private static final int MAX_DELAY = 1200000;

	@Inject
	private CommandDAO commandDAO;

	@Inject
	private TwitchChat twitch;

	private Random random;

	private Set<Command> commands;
	private Map<TimedCommandEntity, Timer> timedCommands;

	@PostConstruct
	public void init() {
		random = new Random();
		commands = new HashSet<>();
		timedCommands = new HashMap<>();
		reload();
	}

	private void reload() {
		LOG.info("Reloading commands...");
		commands.clear();
		timedCommands.values().forEach(Timer::cancel);
		timedCommands.clear();

		commands.add(new HiCommand());
		commands.addAll(fetchAllCommands());
		startTimedCommands();
		LOG.info("[SUCCESS] -> Reloading commands");
	}

	private List<CustomCommand> fetchAllCommands() {
		List<CustomCommandEntity> customCommands = commandDAO.fetchCustomCommands();
		customCommands.forEach(c -> LOG.info("[SUCCESS] -> Loading custom command '{}'", c.getCommand()));
		return customCommands.stream()//
				.map(CustomCommand::new)//
				.collect(Collectors.toList());
	}

	private void startTimedCommands() {
		commandDAO.fetchTimedCommands().forEach(this::createTimer);
	}

	private void createTimer(TimedCommandEntity command) {
		String commandName = command.getCommand();
		int interval = command.getTimeInSeconds();
		int initialDelay = random.nextInt(MAX_DELAY);

		Timer timer = new Timer(String.format("%s_%s_Timer", commandName, interval), true);
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				handleEvent(new CommandMessageEvent(new CommandMessage("", commandName, new ArrayList<>())));
			}
		}, initialDelay, interval * 1000L);
		timedCommands.put(command, timer);

		LOG.info("[SUCCESS] -> Starting timer for command '{}': initial delay: {} seconds, interval: {} seconds",
				commandName, initialDelay / 1000, interval);
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(twitch, ((CommandMessageEvent) event).getMessage()));
		} else if (event instanceof CommandsChangedEvent) {
			reload();
		}
	}

}
