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
import de.herglotz.twitch.events.listeners.CommandHandler;
import de.herglotz.twitch.events.listeners.MessageLogger;

public class TwitchApiFacade {

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
			Socket twitchApi = SSLSocketFactory.getDefault().createSocket(TwitchConstants.TWITCH_API_ADRESS,
					TwitchConstants.TWITCH_API_PORT);
			PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(twitchApi.getOutputStream(), Charset.forName("UTF-8")));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(twitchApi.getInputStream(), Charset.forName("UTF-8")));

			new Thread(new TwitchChatReader(reader)).start();
			twitchChatWriter = new TwitchChatWriter(writer);

			writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
			writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
			writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
			writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, "flitzpiepe96"));
			writer.flush();

			connected = true;
		} catch (IOException e) {
			System.exit(1);
		}
	}

	private void registerListeners() {
		EventBus.instance().register(new MessageLogger());
		EventBus.instance().register(new CommandHandler());
	}

	public TwitchChatWriter getWriter() {
		return twitchChatWriter;
	}

}