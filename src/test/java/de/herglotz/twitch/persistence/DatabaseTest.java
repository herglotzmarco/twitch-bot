package de.herglotz.twitch.persistence;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.herglotz.twitch.persistence.entities.CustomCommandEntity;

public class DatabaseTest {

	private Database undertest;

	@Before
	public void init() {
		undertest = new Database("testing");
	}

	@Test
	public void testPersistAndSelectEntity() throws Exception {
		List<CustomCommandEntity> result = undertest.findAll(CustomCommandEntity.class);
		assertEquals(0, result.size());

		CustomCommandEntity entity = new CustomCommandEntity();
		entity.setCommand("test");
		entity.setMessage("more test");
		undertest.persist(entity);

		result = undertest.findAll(CustomCommandEntity.class);
		assertEquals(1, result.size());
		assertEquals("test", result.get(0).getCommand());
		assertEquals("more test", result.get(0).getMessage());
	}

	@Test
	public void testPersistingMultipleEntities() throws Exception {
		List<CustomCommandEntity> result = undertest.findAll(CustomCommandEntity.class);
		assertEquals(0, result.size());

		CustomCommandEntity entity = new CustomCommandEntity();
		entity.setCommand("test");
		entity.setMessage("more test");
		CustomCommandEntity entity2 = new CustomCommandEntity();
		entity2.setCommand("something");
		entity2.setMessage("more something");
		undertest.persist(entity);
		undertest.persist(entity2);

		result = undertest.findAll(CustomCommandEntity.class);
		assertEquals(2, result.size());
	}

}
