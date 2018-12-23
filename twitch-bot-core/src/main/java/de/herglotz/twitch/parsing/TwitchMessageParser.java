package de.herglotz.twitch.parsing;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.messages.ChatMessage;
import de.herglotz.twitch.messages.Message;
import de.herglotz.twitch.messages.PingMessage;
import de.herglotz.twitch.utils.ExtensionLoader;

public class TwitchMessageParser {

	private Collection<IMessageParserExtension> parserExtensions;

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
		for (IMessageParserExtension parser : loadExtensions()) {
			if (parser.isResponsible(message)) {
				return parser.parse(username, targetChannel, message);
			}
		}

		return new ChatMessage(username, targetChannel, message);
	}

	private Collection<IMessageParserExtension> loadExtensions() {
		if (parserExtensions == null) {
			parserExtensions = ExtensionLoader.loadExtensions(IMessageParserExtension.class, "de.herglotz.twitch");
		}
		return parserExtensions;
	}

}
