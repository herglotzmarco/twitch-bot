package de.herglotz.twitch.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class H2Database implements Database {

	private EntityManager entityManager;

	public H2Database(String persistenceUnit) {
		entityManager = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz) {
		return entityManager.createQuery("SELECT x from " + clazz.getSimpleName() + " x", clazz).getResultList();
	}

	@Override
	public void persist(Object entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

	@Override
	public void delete(Object entity) {
		entityManager.getTransaction().begin();
		entityManager.remove(entity);
		entityManager.getTransaction().commit();
	}

}
