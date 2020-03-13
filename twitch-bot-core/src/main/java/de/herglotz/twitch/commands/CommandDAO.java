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
				addOrUpdateCustomCommand(manageEvent.getCommand(), manageEvent.getMessage());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case REMOVE:
				deleteCustomCommand(manageEvent.getCommand());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case UPDATE_TIMED:
				addOrUpdateTimedCommand(manageEvent.getCommand(), manageEvent.getTimeInSeconds());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			case REMOVE_TIMED:
				deleteTimedCommand(manageEvent.getCommand());
				eventHandler.fire(new CommandsChangedEvent());
				break;
			default:
				throw new IllegalArgumentException("Unexpected operation: " + manageEvent.getOperation());
			}
		}
	}

	private void addOrUpdateCustomCommand(String command, String message) {
		Optional<CustomCommandEntity> existingCommand = fetchCustomCommand(command);
		if (existingCommand.isPresent()) {
			updateCustomCommand(existingCommand.get(), message);
		} else {
			addCustomCommand(command, message);
		}
	}

	private void addCustomCommand(String command, String message) {
		entityManager.getTransaction().begin();
		entityManager.persist(new CustomCommandEntity(command, message));
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Adding new command '{}'", command);
	}

	private void updateCustomCommand(CustomCommandEntity entity, String message) {
		entityManager.getTransaction().begin();
		entity.setMessage(message);
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Updating command '{}'", entity.getCommand());
	}

	private void deleteCustomCommand(String command) {
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("DELETE FROM CustomCommandEntity c WHERE c.command = :command");
		query.setParameter("command", command);
		query.executeUpdate();
		entityManager.getTransaction().commit();
		LOG.info("[SUCCESS] -> Removing command '{}'", command);
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
