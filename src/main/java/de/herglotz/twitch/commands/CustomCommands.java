package de.herglotz.twitch.commands;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class CustomCommands implements Command {

	private Map<String, CustomCommand> commandMap;

	public CustomCommands(Collection<CustomCommandEntity> commands) {
		commandMap = commands.stream().collect(Collectors.toMap(CustomCommandEntity::getCommand, CustomCommand::new));
	}

	@Override
	public void run(TwitchChatWriter writer, CommandMessage commandMessage) {
		CustomCommand command = commandMap.get(commandMessage.getCommand());
		if (command != null) {
			command.run(writer, commandMessage, new CustomCommandParser());
		}
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return commandMap.keySet().contains(commandMessage);
	}

}
