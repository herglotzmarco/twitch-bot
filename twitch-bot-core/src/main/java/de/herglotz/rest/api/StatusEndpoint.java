package de.herglotz.rest.api;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.put;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.ApplicationStatus;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.manage.ShutdownEvent;
import de.herglotz.twitch.events.manage.StartupEvent;
import io.javalin.Javalin;
import io.javalin.http.Context;

@ApplicationScoped
public class StatusEndpoint implements RESTEndoint {

	private static final Logger LOG = LoggerFactory.getLogger(StatusEndpoint.class);

	@Inject
	private Event<TwitchEvent> eventHandler;

	@Inject
	private ApplicationStatus status;

	@Override
	public void start(Javalin api) {
		api.routes(() -> {
			path("status", () -> {
				get(this::getStatus);
				path("start", () -> {
					put(this::startServices);
				});
				path("stop", () -> {
					put(this::stopServices);
				});
			});
		});
	}

	private void getStatus(Context ctx) {
		ctx.json(new StatusResponse(status.isRunning() ? ServicesStatus.RUNNING : ServicesStatus.STOPPED));
		LOG.info("[200] -> getStatus");
	}

	private void startServices(Context ctx) {
		eventHandler.fire(new StartupEvent());
		LOG.info("[200] -> startServices");
	}

	private void stopServices(Context ctx) {
		eventHandler.fire(new ShutdownEvent());
		LOG.info("[200] -> stopServices");
	}

	public class StatusResponse {
		public ServicesStatus status;

		public StatusResponse(ServicesStatus status) {
			this.status = status;
		}
	}

	public enum ServicesStatus {
		STOPPED, RUNNING;
	}

}
