package de.herglotz.twitch.credentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CredentialProvider {

	private Properties properties;

	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try (InputStream is = new FileInputStream(new File("credentials.properties"))) {
				properties.load(is);
			} catch (IOException e) {
				throw new InvalidPropertiesFileException("The given file is not a valid properties file", e);
			}
		}
		return properties;
	}

	public String getBotUsername() {
		return getProperties().getProperty("USERNAME", "");
	}

	public String getOAuthToken() {
		return getProperties().getProperty("OAUTH_TOKEN", "");
	}

	public String getApiClientId() {
		return getProperties().getProperty("CLIENT_ID", "");
	}

	public String getTargetChannel() {
		return getProperties().getProperty("TARGET_CHANNEL", "");
	}

}
