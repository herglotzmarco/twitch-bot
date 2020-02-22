package de.herglotz.rest.api;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.StartupListener;
import io.javalin.Javalin;

@ApplicationScoped
public class RestApi implements StartupListener {

	private static final Logger LOG = LoggerFactory.getLogger(RestApi.class);

	@Inject
	private Instance<RESTEndoint> endpoints;

	@Override
	public void onStart() {
		LOG.info("Starting REST Api...");
		Javalin api = Javalin.create().start(7000);
		endpoints.forEach(endpoint -> endpoint.start(api));
		LOG.info("[SUCCESS] -> Starting REST Api");
	}

}
