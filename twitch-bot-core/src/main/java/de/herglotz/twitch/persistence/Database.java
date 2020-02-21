package de.herglotz.twitch.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@ApplicationScoped
public class Database {

	private EntityManager entityManager;

	public Database() {
		entityManager = Persistence.createEntityManagerFactory("production").createEntityManager();
	}

	@Produces
	@RequestScoped
	public EntityManager produceEntityManager() {
		return entityManager;
	}

}
