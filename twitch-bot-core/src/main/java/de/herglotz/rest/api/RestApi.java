package de.herglotz.rest.api;

import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.IStartupListener;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.core.util.Header;

@ApplicationScoped
public class RestApi implements IStartupListener {

	private static final String FRONTEND_ROOT = "/web-content/angular";
	private static final String INDEX_HTML = FRONTEND_ROOT + "/index.html";

	private static final Logger LOG = LoggerFactory.getLogger(RestApi.class);

	@Inject
	private Instance<RESTEndoint> endpoints;

	@Override
	public void onStart() {
		LOG.info("Starting REST Api...");
		Javalin api = Javalin.create(this::serveWebUI).start(7000);
		configureCORS(api);
		endpoints.forEach(endpoint -> endpoint.start(api));
		Runtime.getRuntime().addShutdownHook(new Thread(api::stop));
		LOG.info("[SUCCESS] -> Starting REST Api");
	}

	private void serveWebUI(JavalinConfig config) {
		// angular content will be copied here during build
		if (Paths.get(INDEX_HTML).toFile().exists()) {
			LOG.info("front-end sources found");
			config.addStaticFiles(FRONTEND_ROOT);
			config.addSinglePageRoot("/", INDEX_HTML);
		} else {
			LOG.warn("front-end sources not found. Web-UI will not be available");
		}
	}

	private void configureCORS(Javalin api) {
		// needed for angular dev only
		api.before(ctx -> ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));
		api.before(ctx -> ctx.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "PUT, GET, POST, DELETE"));
		api.before(ctx -> ctx.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "content-type"));
		api.options("/*", ctx -> ctx.status(200));
	}

}
