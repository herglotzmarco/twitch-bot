package de.herglotz.twitch.api.irc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitchMessageParser {

	private static final String PRVMSG_FORMAT = ":(.*)!.*@.*.tmi.twitch.tv PRIVMSG #(.*) :(.*)";

	public ChatMessage parse(String line) {
		Pattern pattern = Pattern.compile(PRVMSG_FORMAT);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(String.format("This message was not in the expected format: %s ", line));
		}
		return new ChatMessage(matcher.group(1), matcher.group(2), matcher.group(3));
	}

}
