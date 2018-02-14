package de.herglotz.twitch.credentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileCredentialProvider implements CredentialProvider {

	private Properties properties;

	public FileCredentialProvider(File file) throws InvalidPropertiesFileException {
		properties = new Properties();
		try (InputStream is = new FileInputStream(file)) {
			properties.load(is);
		} catch (IOException e) {
			throw new InvalidPropertiesFileException("The given file is not a valid properties file", e);
		}
	}

	@Override
	public String getBotUsername() {
		return properties.getProperty("USERNAME", "");
	}

	@Override
	public String getOAuthToken() {
		return properties.getProperty("OAUTH_TOKEN", "");
	}

	@Override
	public String getApiClientId() {
		return properties.getProperty("CLIENT_ID", "");
	}

}
