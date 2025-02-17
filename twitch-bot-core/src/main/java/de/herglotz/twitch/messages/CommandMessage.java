package de.herglotz.twitch.messages;

import java.util.List;
import java.util.stream.Collectors;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.message.CommandMessageEvent;

public class CommandMessage extends ChatMessage {

	public static final String COMMAND_PREFIX = "!";
	private String command;
	private List<String> parameters;

	public CommandMessage(String username, String command, List<String> parameters) {
		super(username, COMMAND_PREFIX + command + " " + parameters.stream().collect(Collectors.joining(" ")));
		this.command = command;
		this.parameters = parameters;
	}

	@Override
	public TwitchEvent toEvent() {
		return new CommandMessageEvent(this);
	}

	public String getCommand() {
		return command;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public String user() {
		return getUsername();
	}

	@Override
	public String toString() {
		return "CommandMessage [username=" + getUsername() + ", message=" + getMessage() + "]";
	}

}
