package de.herglotz;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.manage.ShutdownEvent;
import de.herglotz.twitch.events.manage.StartupEvent;

@ApplicationScoped
public class ApplicationStatus {

	private boolean running;

	public boolean isRunning() {
		return running;
	}

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof StartupEvent) {
			running = true;
		} else if (event instanceof ShutdownEvent) {
			running = false;
		}
	}
}
