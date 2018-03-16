package de.herglotz.twitch.events.listeners;

import de.herglotz.twitch.events.Event;

@FunctionalInterface
public interface EventListener {

	void handleEvent(Event event);

}
