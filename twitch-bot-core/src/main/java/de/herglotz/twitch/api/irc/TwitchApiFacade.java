package de.herglotz.twitch.api.irc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class TwitchApiFacade extends TwitchApi {

	private OutputStream outputStream;
	private InputStream inputStream;

	public TwitchApiFacade() {
		try {
			Socket twitchApi = SSLSocketFactory.getDefault().createSocket(TwitchConstants.TWITCH_API_ADRESS,
					TwitchConstants.TWITCH_API_PORT);
			outputStream = twitchApi.getOutputStream();
			inputStream = twitchApi.getInputStream();
		} catch (IOException e) {
			System.exit(1);
		}
	}

	@Override
	protected InputStream getInputStream() {
		return inputStream;
	}

	@Override
	protected OutputStream getOutputStream() {
		return outputStream;
	}

}
