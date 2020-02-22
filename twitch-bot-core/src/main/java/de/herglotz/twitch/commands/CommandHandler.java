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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.custom.CustomCommand;
import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.CommandMessageEvent;
import de.herglotz.twitch.events.ManageCommandsEvent;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.CommandMessage;

@ApplicationScoped
public class CommandHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CommandHandler.class);
	private static final int MAX_DELAY = 1200000;

	@Inject
	private EntityManager entityManager;

	@Inject
	private TwitchChat twitch;

	private Random random;

	private Set<Command> commands;
	private Map<String, Timer> timedCommands;

	@PostConstruct
	public void init() {
		random = new Random();
		commands = new HashSet<>();
		timedCommands = new HashMap<>();
		reload();
	}

	public void reload() {
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
		List<CustomCommandEntity> customCommands = entityManager
				.createQuery("SELECT c FROM CustomCommandEntity c", CustomCommandEntity.class)//
				.getResultList();
		customCommands.forEach(c -> LOG.info("[SUCCESS] -> Loading custom command '{}'", c.getCommand()));
		return customCommands.stream()//
				.map(CustomCommand::new)//
				.collect(Collectors.toList());
	}

	private void startTimedCommands() {
		fetchAllTimedCommands().forEach(this::createTimer);
	}

	private List<TimedCommandEntity> fetchAllTimedCommands() {
		return entityManager.createQuery("SELECT c FROM TimedCommandEntity c", TimedCommandEntity.class)//
				.getResultList();
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
		timedCommands.put(commandName, timer);

		LOG.info("[SUCCESS] -> Starting timer for command '{}': initial delay: {} seconds, interval: {} seconds",
				commandName, initialDelay / 1000, interval);
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(twitch, ((CommandMessageEvent) event).getMessage()));
		} else if (event instanceof ManageCommandsEvent) {
			ManageCommandsEvent manageEvent = (ManageCommandsEvent) event;
			switch (manageEvent.getOperation()) {
			case ADD:
				entityManager.getTransaction().begin();
				entityManager.persist(new CustomCommandEntity(manageEvent.getCommand(), manageEvent.getMessage()));
				entityManager.getTransaction().commit();
				LOG.info("[SUCCESS] -> Adding new command '{}'", manageEvent.getCommand());
				break;
			case REMOVE:
				entityManager.getTransaction().begin();
				Query query = entityManager.createQuery("DELETE FROM CustomCommandEntity c WHERE c.command = :command");
				query.setParameter("command", manageEvent.getCommand());
				query.executeUpdate();
				entityManager.getTransaction().commit();
				LOG.info("[SUCCESS] -> Removing command '{}'", manageEvent.getCommand());
				break;
			case TIMED:
				entityManager.getTransaction().begin();
				entityManager.persist(new TimedCommandEntity(manageEvent.getCommand(), manageEvent.getTimeInSeconds()));
				entityManager.getTransaction().commit();
				LOG.info("[SUCCESS] -> Adding new command '{}'", manageEvent.getCommand());
				break;
			default:
				throw new IllegalArgumentException("Unexpected operation: " + manageEvent.getOperation());
			}
			reload();
		}
	}

}
