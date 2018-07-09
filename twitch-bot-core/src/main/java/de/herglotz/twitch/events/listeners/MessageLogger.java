package de.herglotz.twitch.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.events.Event;
import de.herglotz.twitch.events.EventListener;
import de.herglotz.twitch.events.MessageEvent;

public class MessageLogger implements EventListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageLogger.class);

	@Override
	public void handleEvent(Event event) {
		if (event instanceof MessageEvent) {
			LOG.info(((MessageEvent) event).getMessage().toString());
		}
	}

}
