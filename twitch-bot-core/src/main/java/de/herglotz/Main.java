package de.herglotz;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import de.herglotz.twitch.api.irc.TwitchApi;

public class Main {

	public static void main(String[] args) {
		SeContainer container = SeContainerInitializer.newInstance().disableDiscovery().addPackages(true, Main.class)
				.initialize();
		container.select(TwitchApi.class).get().connect();
	}

}
