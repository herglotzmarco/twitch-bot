package de.herglotz.twitch.main;

import java.io.File;

import de.herglotz.twitch.api.irc.TwitchApiFacade;
import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.credentials.FileCredentialProvider;
import de.herglotz.twitch.credentials.InvalidPropertiesFileException;

public class Main {

	public static void main(String[] args) throws InvalidPropertiesFileException {
		CredentialProvider provider = new FileCredentialProvider(new File("credentials.properties"));
		TwitchApiFacade twitchApi = TwitchApiFacade.instance();
		twitchApi.connect(provider);
	}

}
