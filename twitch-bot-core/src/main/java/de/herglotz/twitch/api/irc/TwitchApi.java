package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.AlreadyConnectedException;
import java.nio.charset.Charset;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.EventListener;
import de.herglotz.twitch.events.PingMessageEvent;
import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.events.listeners.MessageLogger;
import de.herglotz.twitch.module.IModule;
import de.herglotz.twitch.persistence.Database;
import de.herglotz.twitch.utils.ExtensionLoader;

public abstract class TwitchApi implements EventListener {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchApi.class);

	private boolean connected;

	private TwitchChatWriter twitchChatWriter;

	public TwitchApi() {
		connected = false;
	}

	public void connect(CredentialProvider credentialProvider, Database database) {
		Preconditions.checkNotNull(credentialProvider);
		if (connected) {
			throw new AlreadyConnectedException();
		}

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), Charset.forName("UTF-8")));
		BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(), Charset.forName("UTF-8")));

		TwitchChatReader.start(reader);
		twitchChatWriter = new TwitchChatWriter(writer);

		writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
		writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
		writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
		writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, "flitzpiepe96"));
		writer.flush();

		connected = true;
		EventBus.instance().register(new MessageLogger());
		EventBus.instance().register(this);
		startModules(database, twitchChatWriter);
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof PingMessageEvent) {
			twitchChatWriter.sendRawMessage(TwitchConstants.TWITCH_API_PONG);
		}
	}

	private void startModules(Database database, TwitchChatWriter writer) {
		for (IModule module : findModules()) {
			LOG.info("Starting module {}", module.getId());
			module.startup(database, writer);
			LOG.info("Module {} started", module.getId());
		}
	}

	private Collection<IModule> findModules() {
		return ExtensionLoader.loadExtensions(IModule.class, "de.herglotz.twitch");
	}

	protected abstract InputStream getInputStream();

	protected abstract OutputStream getOutputStream();

}