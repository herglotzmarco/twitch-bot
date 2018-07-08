package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.AlreadyConnectedException;
import java.nio.charset.Charset;

import com.google.common.base.Preconditions;

import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.listeners.CommandHandler;
import de.herglotz.twitch.events.listeners.MessageLogger;
import de.herglotz.twitch.persistence.Database;

public abstract class TwitchApi {

	private static TwitchApi instance;

	private TwitchChatWriter twitchChatWriter;
	private boolean connected;

	public static TwitchApi instance() {
		if (instance == null)
			instance = new TwitchApiFacade();
		return instance;
	}

	protected TwitchApi() {
		connected = false;
	}

	public void connect(CredentialProvider credentialProvider, Database database) {
		Preconditions.checkNotNull(credentialProvider);
		if (connected)
			throw new AlreadyConnectedException();

		registerListeners(database);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), Charset.forName("UTF-8")));
		BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(), Charset.forName("UTF-8")));

		new Thread(new TwitchChatReader(reader)).start();
		twitchChatWriter = new TwitchChatWriter(writer);

		writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
		writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
		writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
		writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, "flitzpiepe96"));
		writer.flush();

		connected = true;
	}

	protected void registerListeners(Database database) {
		EventBus.instance().register(new MessageLogger());
		EventBus.instance().register(new CommandHandler(database));
	}

	public TwitchChatWriter getWriter() {
		return twitchChatWriter;
	}

	protected abstract InputStream getInputStream();

	protected abstract OutputStream getOutputStream();

}