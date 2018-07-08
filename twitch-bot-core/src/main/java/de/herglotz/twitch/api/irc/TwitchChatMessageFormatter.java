package de.herglotz.twitch.api.irc;

public class TwitchChatMessageFormatter {

	private TwitchChatMessageFormatter() {
	}

	public static String format(String targetChannel, String message) {
		return String.format(TwitchConstants.PRVMSG_SEND_FORMAT, targetChannel, message);
	}

}
