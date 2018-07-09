package de.herglotz.twitch.persistence;

import java.util.List;

public interface Database {

	<T> List<T> findAll(Class<T> clazz);

	void persist(Object entity);

	void delete(Object entity);

}
