package de.herglotz.twitch.commands;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.change.CommandsChangedEvent;
import de.herglotz.twitch.events.manage.ManageCommandsEvent;
import de.herglotz.twitch.persistence.QueryExecutor;

@ApplicationScoped
public class CommandDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CommandDAO.class);

	@Inject
	private QueryExecutor database;

	@Inject
	private Event<TwitchEvent> eventHandler;

	public List<CustomCommandEntity> fetchCustomCommands() {
		return database.list(CustomCommandEntity.class).select("SELECT * FROM CustomCommandEntity c");
	}

	public Optional<CustomCommandEntity> fetchCustomCommand(String command) {
		return database.optional(CustomCommandEntity.class)
				.select("SELECT * FROM CustomCommandEntity c WHERE c.command = ?", command);
	}

	public List<TimedCommandEntity> fetchTimedCommands() {
		return database.list(TimedCommandEntity.class).select("SELECT * FROM TimedCommandEntity c");
	}

	public Optional<TimedCommandEntity> fetchTimedCommand(String command) {
		return database.optional(TimedCommandEntity.class)
				.select("SELECT * FROM TimedCommandEntity c WHERE c.command = ?", command);
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof ManageCommandsEvent) {
			ManageCommandsEvent manageEvent = (ManageCommandsEvent) event;
			switch (manageEvent.getOperation()) {
			case UPDATE:
				addOrUpdateCustomCommand(manageEvent.getAffectedCommand(), manageEvent.getUpdatedCommand());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case REMOVE:
				deleteCustomCommand(manageEvent.getAffectedCommand());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case UPDATE_TIMED:
				addOrUpdateTimedCommand(manageEvent.getAffectedCommand(), manageEvent.getTimeInSeconds());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case REMOVE_TIMED:
				deleteTimedCommand(manageEvent.getAffectedCommand());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			default:
				throw new IllegalArgumentException("Unexpected operation: " + manageEvent.getOperation());
			}
		}
	}

	private void addOrUpdateCustomCommand(String affectedCommand, CustomCommandEntity updatedCommand) {
		Optional<CustomCommandEntity> existingCommand = fetchCustomCommand(affectedCommand);
		if (existingCommand.isPresent()) {
			updateCustomCommand(existingCommand.get(), updatedCommand);
		} else {
			addCustomCommand(updatedCommand);
		}
	}

	private void addCustomCommand(CustomCommandEntity updatedCommand) {
		database.insert("INSERT INTO CustomCommandEntity (command, message) VALUES (?, ?)", updatedCommand.getCommand(),
				updatedCommand.getMessage());
		LOG.info("[SUCCESS] -> Adding new command '{}'", updatedCommand);
	}

	private void updateCustomCommand(CustomCommandEntity affectedCommand, CustomCommandEntity updatedCommand) {
		if (affectedCommand.getCommand().equals(updatedCommand.getCommand())) {
			// update
			database.update("UPDATE CustomCommandEntity c WHERE c.command = ? SET c.message = ?",
					affectedCommand.getCommand(), updatedCommand.getMessage());
		} else {
			// name changed -> delete old and save new
			deleteCustomCommand(affectedCommand.getCommand());
			addCustomCommand(updatedCommand);
		}
		LOG.info("[SUCCESS] -> Updating command '{}'", affectedCommand.getCommand());
	}

	private void deleteCustomCommand(String affectedCommand) {
		Optional<CustomCommandEntity> command = fetchCustomCommand(affectedCommand);
		if (command.isPresent()) {
			database.delete("DELETE FROM CustomCommandEntity c WHERE c.command = ?", affectedCommand);
			LOG.info("[SUCCESS] -> Removing command '{}'", affectedCommand);
		} else {
			LOG.info("[FAILED] -> Removing command '{}'. Command does not exist", affectedCommand);
		}
	}

	private void addOrUpdateTimedCommand(String command, int timeInSeconds) {
		Optional<TimedCommandEntity> existingTimedCommand = fetchTimedCommand(command);
		if (existingTimedCommand.isPresent()) {
			updateTimedCommand(existingTimedCommand.get(), timeInSeconds);
		} else {
			addTimedCommand(command, timeInSeconds);
		}
	}

	private void addTimedCommand(String command, int timeInSeconds) {
		database.insert("INSERT INTO TimedCommandEntity (command, timeInSeconds) VALUES (?, ?)", command,
				timeInSeconds);
		LOG.info("[SUCCESS] -> Adding timed command '{}'", command);
	}

	private void updateTimedCommand(TimedCommandEntity entity, int timeInSeconds) {
		database.insert("UPDATE TimedCommandEntity c WHERE c.command = ? SET timeInSeconds = ?", entity.getCommand(),
				timeInSeconds);
		LOG.info("[SUCCESS] -> Updating timed command '{}'", entity.getCommand());
	}

	private void deleteTimedCommand(String command) {
		database.delete("DELETE FROM TimedCommandEntity c WHERE c.command = ?", command);
		LOG.info("[SUCCESS] -> Removing timed command '{}'", command);
	}

}
