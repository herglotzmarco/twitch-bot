package de.herglotz.twitch.persistence;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private static Database instance;

	public static Database instance() {
		if (instance == null)
			instance = new Database();
		return instance;
	}

	private Database() {
	}

	public <T> List<T> findAll(Class<T> clazz) {
		return new ArrayList<>();
	}

}
