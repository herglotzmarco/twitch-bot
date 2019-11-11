package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.PingMessageEvent;
import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.events.TwitchEvent;

@ApplicationScoped
public class TwitchApi {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchApi.class);

	@Inject
	private CredentialProvider credentialProvider;

	@Inject
	private Event<TwitchEvent> eventHandler;

	private TwitchChatWriter twitchChatWriter;

	public void connect() {
		LOG.info("Twitch Bot starting up...");

		try {
			Socket twitchApi = SSLSocketFactory.getDefault().createSocket(TwitchConstants.TWITCH_API_ADRESS,
					TwitchConstants.TWITCH_API_PORT);
			OutputStream outputStream = twitchApi.getOutputStream();
			InputStream inputStream = twitchApi.getInputStream();

			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

			TwitchChatReader.start(reader, eventHandler);
			twitchChatWriter = new TwitchChatWriter(writer);

			writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
			writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
			writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
			writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, "flitzpiepe96"));
			writer.flush();

			LOG.info("Twitch Bot started");
		} catch (IOException e) {
			System.exit(1);
		}
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof PingMessageEvent) {
			twitchChatWriter.sendRawMessage(TwitchConstants.TWITCH_API_PONG);
		}
	}

	@Produces
	@ApplicationScoped
	public TwitchChatWriter getTwitchWriter() {
		return twitchChatWriter;
	}

}