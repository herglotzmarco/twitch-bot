package de.herglotz.twitch.persistence;

import java.beans.PropertyVetoException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@ApplicationScoped
public class Database {

	private static final Logger LOG = LoggerFactory.getLogger(Database.class);

	private ComboPooledDataSource datasource;

	@PostConstruct
	public void init() {
		try {
			this.datasource = new ComboPooledDataSource();
			this.datasource.setDriverClass("org.h2.Driver");
			this.datasource.setJdbcUrl("jdbc:h2:file:~/twitchBotDB");
			this.datasource.setUser("sa");
			this.datasource.setPassword("sa");
			this.datasource.setAutoCommitOnClose(true);
		} catch (PropertyVetoException e) {
			LOG.error("Connecting to database failed", e);
			System.exit(1);
		}
	}

	@PreDestroy
	public void teardown() {
		this.datasource.close();
	}

	@Produces
	@RequestScoped
	public QueryExecutor produceEntityManager() {
		return new QueryExecutor(new QueryRunner(datasource));
	}

}
