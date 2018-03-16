package de.herglotz.twitch.events;

import java.util.HashSet;
import java.util.Set;

import de.herglotz.twitch.events.listeners.EventListener;

public class EventBus {

	private static EventBus instance;

	private Set<EventListener> listeners;

	private EventBus() {
		listeners = new HashSet<>();
	}

	public static EventBus instance() {
		if (instance == null)
			instance = new EventBus();
		return instance;
	}

	public void fireEvent(Event event) {
		listeners.forEach(listener -> listener.handleEvent(event));
	}

	public void register(EventListener listener) {
		listeners.add(listener);
	}

	public void unregister(EventListener listener) {
		listeners.remove(listener);
	}

}
