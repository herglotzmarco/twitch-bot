package de.herglotz.twitch.persistence;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutor.class);

	private QueryRunner queryRunner;

	public QueryExecutor(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	public <T> ListSelectQueryExecutor<T> list(Class<T> clazz) {
		return new ListSelectQueryExecutor<>(queryRunner, clazz);
	}

	public <T> OptionalSelectQueryExecutor<T> optional(Class<T> clazz) {
		return new OptionalSelectQueryExecutor<>(queryRunner, clazz);
	}

	public void insert(String sql, Object... params) {
		try {
			this.queryRunner.update(sql, params);
		} catch (SQLException e) {
			LOG.error("Executing INSERT query failed", e);
		}
	}

	public void delete(String sql, Object... params) {
		try {
			this.queryRunner.update(sql, params);
		} catch (SQLException e) {
			LOG.error("Executing DELETE query failed", e);
		}
	}

	public void update(String sql, Object... params) {
		try {
			this.queryRunner.update(sql, params);
		} catch (SQLException e) {
			LOG.error("Executing UPDATE query failed", e);
		}
	}

}
