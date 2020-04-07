package de.herglotz.twitch.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListSelectQueryExecutor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(ListSelectQueryExecutor.class);

	private QueryRunner queryRunner;
	private Class<T> clazz;

	public ListSelectQueryExecutor(QueryRunner queryRunner, Class<T> clazz) {
		this.queryRunner = queryRunner;
		this.clazz = clazz;
	}

	public List<T> select(String sql, Object... params) {
		try {
			return this.queryRunner.query(sql, new BeanListHandler<>(clazz), params);
		} catch (SQLException e) {
			LOG.error("Executing SELECT query failed", e);
			return new ArrayList<>();
		}
	}

}
