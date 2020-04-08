package de.herglotz.twitch.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.jboss.weld.exceptions.IllegalStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction {

	private static final Logger LOG = LoggerFactory.getLogger(Transaction.class);

	private final QueryRunner queryRunner;

	private Connection connection;

	public Transaction(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	public Transaction insert(String sql, Object... params) {
		try {
			Connection c = getConnection();
			this.queryRunner.update(c, sql, params);
		} catch (SQLException e) {
			LOG.error("Executing INSERT query failed", e);
			rollback();
		}
		return this;
	}

	public Transaction delete(String sql, Object... params) {
		try {
			Connection c = getConnection();
			this.queryRunner.update(c, sql, params);
		} catch (SQLException e) {
			LOG.error("Executing DELETE query failed", e);
			rollback();
		}
		return this;
	}

	public Transaction update(String sql, Object... params) {
		try {
			Connection c = getConnection();
			this.queryRunner.update(c, sql, params);
		} catch (SQLException e) {
			LOG.error("Executing UPDATE query failed", e);
			rollback();
		}
		return this;
	}

	public boolean commit() {
		try {
			getConnection().commit();
			return true;
		} catch (SQLException e) {
			LOG.error("Committing transaction failed", e);
			rollback();
			return false;
		} finally {
			close();
		}
	}

	private Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = queryRunner.getDataSource().getConnection();
			connection.setAutoCommit(false);
		}
		return connection;
	}

	private void rollback() {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			LOG.error("Transaction rollback failed", e);
		} finally {
			close();
		}
	}

	private void close() {
		try {
			getConnection().close();
		} catch (SQLException e) {
			throw new IllegalStateException("Closing connection failed", e);
		}
	}
}
