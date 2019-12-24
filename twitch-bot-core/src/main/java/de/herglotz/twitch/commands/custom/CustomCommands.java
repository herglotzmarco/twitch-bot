package de.herglotz.twitch.commands.custom;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.messages.CommandMessage;

public class CustomCommands implements Command {

	private static final Logger LOG = LoggerFactory.getLogger(CustomCommands.class);

	private Map<String, CustomCommand> commandMap;

	public CustomCommands(Collection<CustomCommandEntity> commands) {
		commandMap = commands.stream().collect(Collectors.toMap(CustomCommandEntity::getCommand, CustomCommand::new));
		LOG.info("Added the following custom commands from database: [{}]",
				commandMap.keySet().stream().collect(Collectors.joining(", ")));
	}

	@Override
	public void run(TwitchChat twitch, CommandMessage commandMessage) {
		CustomCommand command = commandMap.get(commandMessage.getCommand());
		if (command != null) {
			command.run(twitch, commandMessage);
		}
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return commandMap.containsKey(commandMessage);
	}

}
