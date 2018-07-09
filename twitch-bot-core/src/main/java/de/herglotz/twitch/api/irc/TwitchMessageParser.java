package de.herglotz.twitch.api.irc;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.herglotz.twitch.api.irc.messages.ChatMessage;
import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.api.irc.messages.Message;
import de.herglotz.twitch.api.irc.messages.PingMessage;
import de.herglotz.twitch.api.irc.messages.RawMessage;
import de.herglotz.twitch.events.TwitchConstants;

public class TwitchMessageParser {

	public Message parse(String line) {
		if (TwitchConstants.TWITCH_API_PING.equals(line)) {
			return new PingMessage(line);
		}
		Pattern pattern = Pattern.compile(TwitchConstants.PRVMSG_REGEX);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			return new RawMessage(line);
		}
		String message = matcher.group(3);
		if (message.startsWith(CommandMessage.COMMAND_PREFIX)) {
			// TODO Move command parsing to command module
			return parseCommand(matcher.group(1), matcher.group(2), message);
		}
		return new ChatMessage(matcher.group(1), matcher.group(2), message);
	}

	private Message parseCommand(String username, String targetChannel, String message) {
		StringTokenizer tokenizer = new StringTokenizer(message.substring(1), " ");
		String command = tokenizer.nextToken();
		List<String> parameters = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			parameters.add(tokenizer.nextToken());
		}
		return new CommandMessage(username, targetChannel, command, parameters);
	}

}
