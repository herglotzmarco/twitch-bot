package de.herglotz.twitch.commands;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.change.CommandsChangedEvent;
import de.herglotz.twitch.events.manage.ManageCommandsEvent;

@ApplicationScoped
public class CommandDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CommandDAO.class);

	@Inject
	private EntityManager entityManager;

	@Inject
	private Event<TwitchEvent> eventHandler;

	public List<CustomCommandEntity> fetchCustomCommands() {
		return entityManager.createQuery("SELECT c FROM CustomCommandEntity c", CustomCommandEntity.class)
				.getResultList();
	}

	public Optional<CustomCommandEntity> fetchCustomCommand(String command) {
		TypedQuery<CustomCommandEntity> query = entityManager.createQuery("SELECT c FROM CustomCommandEntity c " //
				+ "WHERE c.command = :command", //
				CustomCommandEntity.class);
		query.setParameter("command", command);
		return query.getResultList().stream().findFirst();
	}

	public List<TimedCommandEntity> fetchTimedCommands() {
		return entityManager.createQuery("SELECT c FROM TimedCommandEntity c", TimedCommandEntity.class)//
				.getResultList();
	}

	public Optional<TimedCommandEntity> fetchTimedCommand(String command) {
		TypedQuery<TimedCommandEntity> query = entityManager.createQuery(
				"SELECT c FROM TimedCommandEntity c " + "WHERE c.command = :command", //
				TimedCommandEntity.class);
		query.setParameter("command", command);
		return query.getResultList().stream().findFirst();
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
			addCustomCommand(affectedCommand, updatedCommand);
		}
	}

	private void addCustomCommand(String affectedCommand, CustomCommandEntity updatedCommand) {
		entityManager.getTransaction().begin();
		entityManager.persist(updatedCommand);
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Adding new command '{}'", affectedCommand);
	}

	private void updateCustomCommand(CustomCommandEntity affectedCommand, CustomCommandEntity updatedCommand) {
		if (affectedCommand.getCommand().equals(updatedCommand.getCommand())) {
			// update
			entityManager.getTransaction().begin();
			entityManager.merge(updatedCommand);
			entityManager.getTransaction().commit();
		} else {
			// name changed -> delete old and save new
			entityManager.getTransaction().begin();
			entityManager.remove(affectedCommand);
			entityManager.persist(updatedCommand);
			entityManager.getTransaction().commit();
		}
		LOG.info("[SUCCESS] -> Updating command '{}'", affectedCommand.getCommand());
	}

	private void deleteCustomCommand(String affectedCommand) {
		Optional<CustomCommandEntity> command = fetchCustomCommand(affectedCommand);
		if (command.isPresent()) {
			entityManager.getTransaction().begin();
			entityManager.remove(command.get());
			entityManager.getTransaction().commit();
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
		entityManager.getTransaction().begin();
		entityManager.persist(new TimedCommandEntity(command, timeInSeconds));
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Adding timed command '{}'", command);
	}

	private void updateTimedCommand(TimedCommandEntity entity, int timeInSeconds) {
		entityManager.getTransaction().begin();
		entity.setTimeInSeconds(timeInSeconds);
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Updating timed command '{}'", entity.getCommand());
	}

	private void deleteTimedCommand(String command) {
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("DELETE FROM TimedCommandEntity c WHERE c.command = :command");
		query.setParameter("command", command);
		query.executeUpdate();
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Removing timed command '{}'", command);
	}

}
