package de.herglotz.rest.api;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.put;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.ApplicationStatus;
import de.herglotz.IApplicationStatusProvider;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.events.manage.StopServicesEvent;
import de.herglotz.twitch.events.manage.StartServicesEvent;
import io.javalin.Javalin;
import io.javalin.http.Context;

@ApplicationScoped
public class StatusEndpoint implements RESTEndoint {

	private static final Logger LOG = LoggerFactory.getLogger(StatusEndpoint.class);

	@Inject
	private Event<TwitchEvent> eventHandler;

	@Inject
	private Instance<IApplicationStatusProvider> statusProvider;

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
		ctx.json(new StatusResponse(collectStatus()));
		LOG.info("[200] -> getStatus");
	}

	private void startServices(Context ctx) {
		eventHandler.fire(new StartServicesEvent());
		LOG.info("[200] -> startServices");
	}

	private void stopServices(Context ctx) {
		eventHandler.fire(new StopServicesEvent());
		LOG.info("[200] -> stopServices");
	}

	private ApplicationStatus collectStatus() {
		return statusProvider.stream()//
				.map(IApplicationStatusProvider::getStatus)//
				.reduce(ApplicationStatus::mergeStatus)//
				.orElse(ApplicationStatus.UNSTABLE);
	}

	public class StatusResponse {
		public ApplicationStatus status;

		public StatusResponse(ApplicationStatus status) {
			this.status = status;
		}
	}

}
