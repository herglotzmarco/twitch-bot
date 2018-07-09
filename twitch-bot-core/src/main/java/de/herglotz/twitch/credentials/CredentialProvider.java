package de.herglotz.twitch.credentials;

public interface CredentialProvider {

	String getBotUsername();

	String getOAuthToken();

	String getApiClientId();

}
