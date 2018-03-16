package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import javax.net.ssl.SSLSocketFactory;

import de.herglotz.twitch.credentials.CredentialProvider;

public class TwitchApiFacade {

	private static final String TWITCH_API_ADRESS = "irc.chat.twitch.tv";
	private static final int TWITCH_API_PORT = 443;

	private static final String TWITCH_API_OAUTH = "PASS oauth:%s";
	private static final String TWITCH_API_NICK = "NICK %s";
	private static final String TWITCH_API_JOIN = "JOIN #%s";
	private static final String TWITCH_API_REQCOMMANDS = "CAP REQ :twitch.tv/commands";
	private static final String TWITCH_API_REQTAGS = "CAP REQ :twitch.tv/tags";

	private CredentialProvider credentialProvider;

	public TwitchApiFacade(CredentialProvider credentialProvider) {
		this.credentialProvider = credentialProvider;
	}

	public void connect() {
		try {
			Socket twitchApi = SSLSocketFactory.getDefault().createSocket(TWITCH_API_ADRESS, TWITCH_API_PORT);
			PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(twitchApi.getOutputStream(), Charset.forName("UTF-8")));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(twitchApi.getInputStream(), Charset.forName("UTF-8")));

			TwitchChatWriter twitchChatWriter = new TwitchChatWriter(writer);
			TwitchChatReader twitchChatReader = new TwitchChatReader(reader);

			writer.println(String.format(TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
			writer.println(String.format(TWITCH_API_NICK, credentialProvider.getBotUsername()));
			writer.println(TWITCH_API_REQCOMMANDS);
			writer.println(String.format(TWITCH_API_JOIN, "flitzpiepe96"));
			writer.flush();

			new Thread(twitchChatReader).start();
		} catch (IOException e) {
			System.exit(1);
		}
	}

}