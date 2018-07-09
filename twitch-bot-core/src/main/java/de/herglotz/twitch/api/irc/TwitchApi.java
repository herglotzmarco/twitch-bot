package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.AlreadyConnectedException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.events.listeners.MessageLogger;
import de.herglotz.twitch.module.IModule;
import de.herglotz.twitch.persistence.Database;

public abstract class TwitchApi {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchApi.class);

	private boolean connected;

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
		TwitchChatWriter twitchChatWriter = new TwitchChatWriter(writer);

		writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
		writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
		writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
		writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, "flitzpiepe96"));
		writer.flush();

		connected = true;
		EventBus.instance().register(new MessageLogger());
		startModules(database, twitchChatWriter);
	}

	private void startModules(Database database, TwitchChatWriter writer) {
		findModules().forEach(m -> m.startup(database, writer));
	}

	private List<IModule> findModules() {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setExpandSuperTypes(false).forPackages("de.herglotz.twitch"));
		Set<Class<? extends IModule>> classes = reflections.getSubTypesOf(IModule.class);
		List<IModule> modules = new ArrayList<>();
		for (Class<? extends IModule> clazz : classes) {
			try {
				modules.add(clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Failed to instantiate module {}", clazz.getName(), e);
			}
		}
		return modules;
	}

	protected abstract InputStream getInputStream();

	protected abstract OutputStream getOutputStream();

}