package de.herglotz.rest.api;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.put;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.commands.CommandDAO;
import de.herglotz.twitch.commands.TimedCommandEntity;
import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.manage.ManageCommandsEvent;
import io.javalin.Javalin;
import io.javalin.http.Context;

@ApplicationScoped
public class CommandsEndpoint implements RESTEndoint {

	private static final Logger LOG = LoggerFactory.getLogger(CommandsEndpoint.class);

	@Inject
	private CommandDAO commandDAO;

	@Inject
	private Event<TwitchEvent> eventHandler;

	@Override
	public void start(Javalin api) {
		api.routes(() -> {
			path("commands", () -> {
				get(this::getCommands);
				path(":name", () -> {
					get(this::getCommandWithName);
					put(this::updateCommandMessage);
					delete(this::deleteCommand);
				});
			});
			path("timed", () -> {
				get(this::getTimedCommands);
				path(":name", () -> {
					get(this::getTimedCommandWithName);
					put(this::updateTimedCommand);
					delete(this::deleteTimedCommand);
				});
			});
		});
	}

	private void getCommands(Context ctx) {
		ctx.json(commandDAO.fetchCustomCommands().stream()//
				.map(CustomCommandEntity::getCommand)//
				.collect(toList()));
		LOG.info("[200] -> getAllCommands");
	}

	private void getCommandWithName(Context ctx) {
		Optional<CustomCommandResponse> response = commandDAO.fetchCustomCommand(ctx.pathParam("name"))
				.map(this::response);
		if (response.isPresent()) {
			ctx.json(response.get());
			LOG.info("[200] -> getCommandWithName");
		} else {
			ctx.status(404);
			LOG.info("[404] -> getCommandWithName");
		}
	}

	private void updateCommandMessage(Context ctx) {
		String message = ctx.body();
		String command = ctx.pathParam("name");
		eventHandler.fire(ManageCommandsEvent.update(command, message));
		LOG.info("[200] -> updateCommandMessage");
	}

	private void deleteCommand(Context ctx) {
		eventHandler.fire(ManageCommandsEvent.remove(ctx.pathParam("name")));
		LOG.info("[200] -> deleteCommand");
	}

	private void getTimedCommands(Context ctx) {
		ctx.json(commandDAO.fetchTimedCommands().stream()//
				.map(TimedCommandEntity::getCommand)//
				.collect(toList()));
		LOG.info("[200] -> getTimedCommands");
	}

	private void getTimedCommandWithName(Context ctx) {
		Optional<TimedCommandResponse> response = commandDAO.fetchTimedCommand(ctx.pathParam("name"))
				.map(this::response);
		if (response.isPresent()) {
			ctx.json(response.get());
			LOG.info("[200] -> getTimedCommandWithName");
		} else {
			ctx.status(404);
			LOG.info("[404] -> getTimedCommandWithName");
		}
	}

	private void updateTimedCommand(Context ctx) {
		String timeInSeconds = ctx.body();
		String command = ctx.pathParam("name");
		eventHandler.fire(ManageCommandsEvent.updateTimed(command, Integer.parseInt(timeInSeconds)));
		LOG.info("[200] -> updateTimedCommand");
	}

	private void deleteTimedCommand(Context ctx) {
		eventHandler.fire(ManageCommandsEvent.removeTimed(ctx.pathParam("name")));
		LOG.info("[200] -> deleteTimedCommand");
	}

	private CustomCommandResponse response(CustomCommandEntity command) {
		return new CustomCommandResponse(command.getCommand(), command.getMessage());
	}

	private TimedCommandResponse response(TimedCommandEntity command) {
		return new TimedCommandResponse(command.getCommand(), command.getTimeInSeconds());
	}

	public class CustomCommandResponse {
		public String command;
		public String message;

		public CustomCommandResponse(String command, String message) {
			this.command = command;
			this.message = message;
		}
	}

	public class TimedCommandResponse {
		public String command;
		public int timeInSeconds;

		public TimedCommandResponse(String command, int timeInSeconds) {
			this.command = command;
			this.timeInSeconds = timeInSeconds;
		}
	}

}
