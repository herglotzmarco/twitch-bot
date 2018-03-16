package de.herglotz.twitch.events;

import de.herglotz.twitch.events.listeners.EventListener;

public class CountingEventListener implements EventListener {

	private Class<? extends Event> expected;
	private int counter;

	public CountingEventListener(Class<? extends Event> expected) {
		this.expected = expected;
		this.counter = 0;
	}

	@Override
	public void handleEvent(Event event) {
		if (expected.equals(event.getClass())) {
			counter++;
		}
	}

	public int getCounter() {
		return counter;
	}

}