package de.herglotz.twitch.api.irc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.herglotz.twitch.api.irc.messages.ChatMessage;
import de.herglotz.twitch.api.irc.messages.Message;
import de.herglotz.twitch.api.irc.messages.PingMessage;
import de.herglotz.twitch.api.irc.messages.RawMessage;

public class TwitchMessageParser {

	private static final String PRVMSG_FORMAT = ":(.*)!.*@.*.tmi.twitch.tv PRIVMSG #(.*) :(.*)";
	protected static final String TWITCH_API_PING = "PING :tmi.twitch.tv";

	public Message parse(String line) {
		if (TWITCH_API_PING.equals(line)) {
			return new PingMessage(line);
		}
		Pattern pattern = Pattern.compile(PRVMSG_FORMAT);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			return new RawMessage(line);
		}
		return new ChatMessage(matcher.group(1), matcher.group(2), matcher.group(3));
	}

}
