package de.herglotz.twitch.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.herglotz.twitch.messages.CommandMessage;
import de.herglotz.twitch.messages.Message;

public class CommandMessageParser implements IMessageParserExtension {

	@Override
	public boolean isResponsible(String message) {
		return message.startsWith(CommandMessage.COMMAND_PREFIX);
	}

	@Override
	public Message parse(String username, String targetChannel, String message) {
		StringTokenizer tokenizer = new StringTokenizer(message.substring(1), " ");
		String command = tokenizer.nextToken();
		List<String> parameters = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			parameters.add(tokenizer.nextToken());
		}
		return new CommandMessage(username, targetChannel, command, parameters);
	}

}
