package de.herglotz.twitch.events;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.herglotz.twitch.messages.PingMessage;

public class EventBusTest {

	@Test
	public void testRegisteringHandler() throws Exception {
		EventBus bus = EventBus.instance();
		CountingEventListener listener = new CountingEventListener(PingMessageEvent.class);
		bus.register(listener);
		bus.fireEvent(new PingMessage("").toEvent());
		assertEquals(1, listener.getCounter());
	}

	@Test
	public void testUnregisterHandler() throws Exception {
		EventBus bus = EventBus.instance();
		CountingEventListener listener = new CountingEventListener(PingMessageEvent.class);
		bus.register(listener);
		bus.unregister(listener);
		bus.fireEvent(new PingMessage("").toEvent());
		assertEquals(0, listener.getCounter());
	}

}
