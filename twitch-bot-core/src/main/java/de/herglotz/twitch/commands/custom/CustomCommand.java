package de.herglotz.twitch.commands.custom;

import java.util.Objects;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.messages.CommandMessage;

public class CustomCommand implements Command {

	private String command;
	private String message;

	public CustomCommand(CustomCommandEntity entity) {
		message = entity.getMessage();
		command = entity.getCommand();
	}

	@Override
	public void run(TwitchChat twitch, CommandMessage commandMessage) {
		twitch.sendChatMessage(new CustomCommandParser().parse(message, commandMessage));
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return command.equals(commandMessage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(command, message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CustomCommand other = (CustomCommand) obj;
		return Objects.equals(command, other.command) && Objects.equals(message, other.message);
	}

	@Override
	public String toString() {
		return "CustomCommand [command=" + command + ", message=" + message + "]";
	}

}
