package de.herglotz.twitch.persistence;

import org.apache.commons.dbutils.QueryRunner;

public class QueryExecutor {

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

	public Transaction transaction() {
		return new Transaction(queryRunner);
	}

}
