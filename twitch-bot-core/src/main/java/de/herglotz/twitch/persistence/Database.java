package de.herglotz.twitch.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@ApplicationScoped
public class Database {

	private EntityManager entityManager;

	public Database() {
		entityManager = Persistence.createEntityManagerFactory("production").createEntityManager();
	}

	public <T> List<T> findAll(Class<T> clazz) {
		return entityManager.createQuery("SELECT x from " + clazz.getSimpleName() + " x", clazz).getResultList();
	}

	public void persist(Object entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

	public void delete(Object entity) {
		entityManager.getTransaction().begin();
		entityManager.remove(entity);
		entityManager.getTransaction().commit();
	}

}
