package de.herglotz.twitch.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TestableDatabase implements Database {

	private Set<Object> data;

	public TestableDatabase() {
		data = new HashSet<>();
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz) {
		return (List<T>) data.stream().filter(o -> o.getClass().equals(clazz)).collect(Collectors.toList());
	}

	@Override
	public void persist(Object entity) {
		data.add(entity);
	}

	@Override
	public void delete(Object entity) {
		data.remove(entity);
	}

}
