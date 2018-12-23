package de.herglotz.twitch.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionLoader {

	private static final Logger LOG = LoggerFactory.getLogger(ExtensionLoader.class);

	private ExtensionLoader() {
	}

	public static <T> Collection<T> loadExtensions(Class<T> clazz, String... basePath) {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setExpandSuperTypes(false).forPackages(basePath));
		Set<Class<? extends T>> classes = reflections.getSubTypesOf(clazz);
		List<T> modules = new ArrayList<>();
		for (Class<? extends T> c : classes) {
			try {
				modules.add(c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Failed to instantiate class {}", c.getName(), e);
			}
		}
		return modules;
	}

}
