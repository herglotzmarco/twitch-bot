package de.herglotz.twitch.api.helix;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import de.herglotz.IStartupListener;
import de.herglotz.twitch.credentials.CredentialProvider;

@ApplicationScoped
public class TwitchApiTokenRetriever implements IStartupListener {

	private static final String AUTH_URL = "https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=client_credentials";
	private static final HttpClient client = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();

	@Inject
	private CredentialProvider provider;

	@Override
	public void onStart() {
		getAccessToken();
	}

	public String getAccessToken() {
		HttpRequest request = HttpRequest.newBuilder()//
				.uri(URI.create(String.format(AUTH_URL, provider.getClientId(), provider.getClientSecret())))//
				.POST(BodyPublishers.noBody())//
				.build();

		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				throw new IllegalArgumentException("Login to Twitch API failed with code: " + response.statusCode());
			}
			try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
				JsonObject json = reader.readObject();
				return json.getString("access_token");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalArgumentException("Login to Twitch API failed");
		} catch (IOException e) {
			throw new IllegalArgumentException("Login to Twitch API failed");
		}
	}

}
