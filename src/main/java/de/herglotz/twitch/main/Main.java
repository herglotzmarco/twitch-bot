package de.herglotz.twitch.main;

import java.io.File;

import de.herglotz.twitch.api.irc.TwitchApi;
import de.herglotz.twitch.credentials.CredentialProvider;
import de.herglotz.twitch.credentials.FileCredentialProvider;
import de.herglotz.twitch.credentials.InvalidPropertiesFileException;
import de.herglotz.twitch.persistence.Database;

public class Main {

	public static void main(String[] args) throws InvalidPropertiesFileException {
		CredentialProvider provider = new FileCredentialProvider(new File("credentials.properties"));
		TwitchApi.instance().connect(provider, Database.instance());
	}

}
