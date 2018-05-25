package de.herglotz.twitch.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.google.common.annotations.VisibleForTesting;

import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class Database {

	private static Database instance;

	private EntityManager entityManager;

	public static Database instance() {
		if (instance == null)
			instance = new Database("production");
		return instance;
	}

	@VisibleForTesting
	Database(String persistenceUnit) {
		entityManager = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
	}

	public <T> List<T> findAll(Class<T> clazz) {
		return entityManager.createQuery("SELECT x from " + clazz.getSimpleName() + " x", clazz).getResultList();
	}

	public void persist(CustomCommandEntity entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

}
