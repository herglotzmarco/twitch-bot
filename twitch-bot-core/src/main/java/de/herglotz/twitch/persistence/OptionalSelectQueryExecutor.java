package de.herglotz.twitch.persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionalSelectQueryExecutor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(OptionalSelectQueryExecutor.class);

	private QueryRunner queryRunner;
	private Class<T> clazz;

	public OptionalSelectQueryExecutor(QueryRunner queryRunner, Class<T> clazz) {
		this.queryRunner = queryRunner;
		this.clazz = clazz;
	}

	public Optional<T> select(String sql, Object... params) {
		try {
			List<T> resultList = this.queryRunner.query(sql, new BeanListHandler<>(clazz), params);
			if (resultList.isEmpty()) {
				return Optional.empty();
			} else if (resultList.size() > 1) {
				LOG.warn("Expected single result, but got {} -> {}. Returning first", resultList.size(), resultList);
				return Optional.ofNullable(resultList.get(0));
			} else {
				return Optional.ofNullable(resultList.get(0));
			}
		} catch (SQLException e) {
			LOG.error("Executing SELECT query failed", e);
			return Optional.empty();
		}
	}

}
