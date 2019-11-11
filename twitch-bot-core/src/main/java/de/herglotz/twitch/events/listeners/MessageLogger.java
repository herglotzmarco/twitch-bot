package de.herglotz.twitch.events.listeners;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.events.MessageEvent;
import de.herglotz.twitch.events.TwitchEvent;

@ApplicationScoped
public class MessageLogger {

	private static final Logger LOG = LoggerFactory.getLogger(MessageLogger.class);

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof MessageEvent) {
			LOG.info(((MessageEvent) event).getMessage().toString());
		}
	}

}
