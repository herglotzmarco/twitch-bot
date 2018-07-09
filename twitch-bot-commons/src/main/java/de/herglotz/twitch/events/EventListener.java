package de.herglotz.twitch.events;

@FunctionalInterface
public interface EventListener {

	void handleEvent(Event event);

}
