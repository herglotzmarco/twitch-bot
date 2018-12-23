package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.channels.AlreadyConnectedException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.herglotz.twitch.credentials.FileCredentialProvider;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.messages.PingMessage;
import de.herglotz.twitch.persistence.TestableDatabase;

public class TwitchApiTest {

	private File file;

	@Before
	public void setup() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("test_credentials.properties");
		file = new File(url.toURI());
	}

	@Test(expected = AlreadyConnectedException.class)
	public void testThatConnectionDoesNotWorkTwice() throws Exception {
		TwitchApi instance = new TestableTwitchApi();
		instance.connect(new FileCredentialProvider(file), new TestableDatabase());
		instance.connect(new FileCredentialProvider(file), new TestableDatabase());
	}

	@Test(expected = NullPointerException.class)
	public void testThatCredentialProviderMightNotBeNull() throws Exception {
		TwitchApi instance = new TestableTwitchApi();
		instance.connect(null, new TestableDatabase());
	}

	@Test
	public void testHandshakeProtocol() throws Exception {
		TwitchApi instance = new TestableTwitchApi();
		instance.connect(new FileCredentialProvider(file), new TestableDatabase());

		String written = new String(((ByteArrayOutputStream) instance.getOutputStream()).toByteArray());
		List<String> lines = new BufferedReader(new StringReader(written)).lines().collect(Collectors.toList());
		assertEquals("PASS oauth:someToken", lines.get(0));
		assertEquals("NICK myUsername", lines.get(1));
		assertEquals("CAP REQ :twitch.tv/commands", lines.get(2));
		assertEquals("JOIN #flitzpiepe96", lines.get(3));
	}

	@Test
	public void testWritingPongMessages() throws Exception {
		TwitchApi instance = new TestableTwitchApi();
		instance.connect(new FileCredentialProvider(file), new TestableDatabase());
		instance.handleEvent(new PingMessage("").toEvent());

		String written = new String(((ByteArrayOutputStream) instance.getOutputStream()).toByteArray());
		List<String> lines = new BufferedReader(new StringReader(written)).lines().collect(Collectors.toList());
		assertEquals("PASS oauth:someToken", lines.get(0));
		assertEquals("NICK myUsername", lines.get(1));
		assertEquals("CAP REQ :twitch.tv/commands", lines.get(2));
		assertEquals("JOIN #flitzpiepe96", lines.get(3));
		assertEquals(TwitchConstants.TWITCH_API_PONG, lines.get(4));
	}

	@Test
	public void testListeningToEventBus() throws Exception {
		TwitchApi instance = new TestableTwitchApi();
		instance.connect(new FileCredentialProvider(file), new TestableDatabase());
		EventBus.instance().fireEvent(new PingMessage("").toEvent());

		String written = new String(((ByteArrayOutputStream) instance.getOutputStream()).toByteArray());
		List<String> lines = new BufferedReader(new StringReader(written)).lines().collect(Collectors.toList());
		assertEquals("PASS oauth:someToken", lines.get(0));
		assertEquals("NICK myUsername", lines.get(1));
		assertEquals("CAP REQ :twitch.tv/commands", lines.get(2));
		assertEquals("JOIN #flitzpiepe96", lines.get(3));
		assertEquals(TwitchConstants.TWITCH_API_PONG, lines.get(4));

	}

	private class TestableTwitchApi extends TwitchApi {

		private ByteArrayInputStream input;
		private ByteArrayOutputStream output;

		public TestableTwitchApi() {
			input = new ByteArrayInputStream(new byte[0]);
			output = new ByteArrayOutputStream();
		}

		@Override
		protected InputStream getInputStream() {
			return input;
		}

		@Override
		protected OutputStream getOutputStream() {
			return output;
		}

	}

}
