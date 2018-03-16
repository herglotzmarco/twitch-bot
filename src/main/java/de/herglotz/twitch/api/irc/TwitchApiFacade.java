package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;
import java.nio.charset.Charset;

import javax.net.ssl.SSLSocketFactory;

import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.listeners.MessageLogger;

public class TwitchApiFacade {

	private static final String TWITCH_API_ADRESS = "irc.chat.twitch.tv";
	private static final int TWITCH_API_PORT = 443;

	private static final String TWITCH_API_OAUTH = "PASS oauth:%s";
	private static final String TWITCH_API_NICK = "NICK %s";
	private static final String TWITCH_API_JOIN = "JOIN #%s";
	private static final String TWITCH_API_REQCOMMANDS = "CAP REQ :twitch.tv/commands";
	private static final String TWITCH_API_REQTAGS = "CAP REQ :twitch.tv/tags";

	private static TwitchApiFacade instance;

	private TwitchChatWriter twitchChatWriter;
	private boolean connected;

	public static TwitchApiFacade instance() {
		if (instance == null)
			instance = new TwitchApiFacade();
		return instance;
	}

	private TwitchApiFacade() {
		connected = false;
	}

	public void connect(CredentialProvider credentialProvider) {
		if (connected)
			throw new AlreadyConnectedException();
		try {
			registerListeners();
			Socket twitchApi = SSLSocketFactory.getDefault().createSocket(TWITCH_API_ADRESS, TWITCH_API_PORT);
			PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(twitchApi.getOutputStream(), Charset.forName("UTF-8")));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(twitchApi.getInputStream(), Charset.forName("UTF-8")));

			new Thread(new TwitchChatReader(reader)).start();
			twitchChatWriter = new TwitchChatWriter(writer);

			writer.println(String.format(TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
			writer.println(String.format(TWITCH_API_NICK, credentialProvider.getBotUsername()));
			writer.println(TWITCH_API_REQCOMMANDS);
			writer.println(String.format(TWITCH_API_JOIN, "flitzpiepe96"));
			writer.flush();

			connected = true;
		} catch (IOException e) {
			System.exit(1);
		}
	}

	private void registerListeners() {
		EventBus.instance().register(new MessageLogger());
	}

	public TwitchChatWriter getWriter() {
		return twitchChatWriter;
	}

}