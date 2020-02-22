package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.StartupListener;
import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.events.PingMessageEvent;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.Message;

@ApplicationScoped
public class TwitchChat implements StartupListener {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChat.class);

	@Inject
	private CredentialProvider credentialProvider;

	@Inject
	private Event<TwitchEvent> eventHandler;

	private boolean running = true;
	private String targetChannel = "flitzpiepe96";

	private PrintWriter writer;
	private BufferedReader reader;

	@Override
	public void onStart() {
		new Thread(this::runTwitchChat, "TwitchChat").start();
	}

	private void runTwitchChat() {
		while (running) {
			try (Socket twitchApi = openSocket()) {
				connectToTwitch(twitchApi);
				readMessages();
			} catch (SocketException e) {
				LOG.info("Connection to Twitch IRC lost. Reconnecting...");
			} catch (IOException e) {
				LOG.error("Exception reading Twitch IRC", e);
			} catch (Exception e) {
				LOG.error("Twitch Bot crashed", e);
				System.exit(0);
			}
		}
	}

	private void connectToTwitch(Socket twitchApi) throws IOException {
		LOG.info("Connecting to Twitch IRC...");
		OutputStream outputStream = twitchApi.getOutputStream();
		InputStream inputStream = twitchApi.getInputStream();

		writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
		reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

		writer.println(String.format(TwitchConstants.TWITCH_API_OAUTH, credentialProvider.getOAuthToken()));
		writer.println(String.format(TwitchConstants.TWITCH_API_NICK, credentialProvider.getBotUsername()));
		writer.println(TwitchConstants.TWITCH_API_REQCOMMANDS);
		writer.println(String.format(TwitchConstants.TWITCH_API_JOIN, targetChannel));
		writer.flush();

		LOG.info("[SUCCESS] -> Connecting to Twitch IRC");
	}

	private Socket openSocket() throws IOException {
		return SSLSocketFactory.getDefault().createSocket(TwitchConstants.TWITCH_API_ADRESS,
				TwitchConstants.TWITCH_API_PORT);
	}

	private void readMessages() throws IOException {
		String line;
		TwitchMessageParser parser = new TwitchMessageParser();
		while ((line = reader.readLine()) != null) {
			Message message = parser.parse(line);
			eventHandler.fire(message.toEvent());
		}
	}

	public void sendChatMessage(String message) {
		sendRawMessage(TwitchChatMessageFormatter.format(targetChannel, message));
	}

	public void sendRawMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof PingMessageEvent) {
			sendRawMessage(TwitchConstants.TWITCH_API_PONG);
		}
	}
}