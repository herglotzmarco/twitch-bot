package de.herglotz.twitch.commands;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.custom.CustomCommand;
import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.change.CommandsChangedEvent;
import de.herglotz.twitch.events.manage.ShutdownEvent;
import de.herglotz.twitch.events.manage.StartupEvent;
import de.herglotz.twitch.events.message.CommandMessageEvent;
import de.herglotz.twitch.messages.CommandMessage;

@ApplicationScoped
public class CommandHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CommandHandler.class);
	private static final int MAX_DELAY = 1200000;

	@Inject
	private CommandDAO commandDAO;

	@Inject
	private TwitchChat twitch;

	private Random random = new Random();

	private Set<Command> commands = new HashSet<>();
	private Map<TimedCommandEntity, Timer> timedCommands = new HashMap<>();

	private void reload() {
		LOG.info("Reloading commands...");
		commands.clear();
		commands.add(new HiCommand());
		commands.addAll(fetchAllCommands());

		timedCommands.values().forEach(Timer::cancel);
		timedCommands.clear();
		timedCommands.putAll(startTimedCommands());
		LOG.info("[SUCCESS] -> Reloading commands");
	}

	private List<CustomCommand> fetchAllCommands() {
		List<CustomCommandEntity> customCommands = commandDAO.fetchCustomCommands();
		customCommands.forEach(c -> LOG.info("[SUCCESS] -> Loading custom command '{}'", c.getCommand()));
		return customCommands.stream()//
				.map(CustomCommand::new)//
				.collect(Collectors.toList());
	}

	private Map<TimedCommandEntity, Timer> startTimedCommands() {
		return commandDAO.fetchTimedCommands().stream().collect(toMap(Function.identity(), this::createTimer));
	}

	private Timer createTimer(TimedCommandEntity command) {
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

		LOG.info("[SUCCESS] -> Starting timer for command '{}': initial delay: {} seconds, interval: {} seconds",
				commandName, initialDelay / 1000, interval);
		return timer;
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof StartupEvent) {
			LOG.info("Starting CommandHandler...");
			reload();
			LOG.info("[SUCCESS] -> Starting CommandHandler");
		} else if (event instanceof ShutdownEvent) {
			LOG.info("Stopping CommandHandler...");
			timedCommands.values().forEach(Timer::cancel);
			timedCommands.clear();
			LOG.info("[SUCCESS] -> Stopping CommandHandler");
		} else if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(twitch, ((CommandMessageEvent) event).getMessage()));
		} else if (event instanceof CommandsChangedEvent) {
			reload();
		}
	}

}
