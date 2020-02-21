package de.herglotz.twitch.api.irc;

public class TwitchConstants {

	private TwitchConstants() {
	}

	// Connection Properties
	public static final String TWITCH_API_ADRESS = "irc.chat.twitch.tv";
	public static final int TWITCH_API_PORT = 443;

	// Handshake
	public static final String TWITCH_API_OAUTH = "PASS oauth:%s";
	public static final String TWITCH_API_NICK = "NICK %s";
	public static final String TWITCH_API_JOIN = "JOIN #%s";
	public static final String TWITCH_API_REQCOMMANDS = "CAP REQ :twitch.tv/commands";
	public static final String TWITCH_API_REQTAGS = "CAP REQ :twitch.tv/tags";

	// Keep-alive Connection
	public static final String TWITCH_API_PONG = "PONG :tmi.twitch.tv";
	public static final String TWITCH_API_PING = "PING :tmi.twitch.tv";

	// Message Formats
	public static final String PRVMSG_REGEX = ":(.*)!.*@.*.tmi.twitch.tv PRIVMSG #(.*) :(.*)";
	public static final String PRVMSG_FORMAT = ":%s!%s@%s.tmi.twitch.tv PRIVMSG #%s :%s";
	public static final String PRVMSG_SEND_FORMAT = "PRIVMSG #%s :%s";

}
