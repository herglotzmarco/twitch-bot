package de.herglotz.twitch.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.messages.ChatMessage;
import de.herglotz.twitch.messages.CommandMessage;
import de.herglotz.twitch.messages.Message;
import de.herglotz.twitch.messages.PingMessage;

public class TwitchMessageParser {

	public Message parse(String line) {
		if (TwitchConstants.TWITCH_API_PING.equals(line)) {
			return new PingMessage(line);
		}
		Pattern pattern = Pattern.compile(TwitchConstants.PRVMSG_REGEX);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			return new Message(line);
		}
		String username = matcher.group(1);
		String targetChannel = matcher.group(2);
		String message = matcher.group(3);
		if (isCommand(message)) {
			return parseCommand(username, targetChannel, message);
		}

		return new ChatMessage(username, targetChannel, message);
	}

	public boolean isCommand(String message) {
		return message.startsWith(CommandMessage.COMMAND_PREFIX);
	}

	public Message parseCommand(String username, String targetChannel, String message) {
		StringTokenizer tokenizer = new StringTokenizer(message.substring(1), " ");
		String command = tokenizer.nextToken();
		List<String> parameters = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			parameters.add(tokenizer.nextToken());
		}
		return new CommandMessage(username, targetChannel, command, parameters);
	}

}
