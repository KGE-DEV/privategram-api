package com.garrettestrin.PrivateGram.app;

import com.codahale.metrics.health.HealthCheck;
import com.garrettestrin.PrivateGram.api.UserResource;
import com.garrettestrin.PrivateGram.biz.UserService;
import com.garrettestrin.PrivateGram.data.UserDao;
import com.garrettestrin.PrivateGram.health.DBHealthCheck;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.extern.jbosslog.JBossLog;
import lombok.val;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

/**
 * Guice without Guice.
 *
 * <p>DependencyManager wires up the application. DAOs are connected to the database and
 * dependencies are handed to the objects that use them.
 */
@JBossLog
@Getter
class DependencyManager {
    public final UserResource userResource;

    DependencyManager(PrivateGramConfiguration config, Environment env) {
        log.info("Initializing database pool...");
        // This creates a managed database and creates a JdbiHealthCheck
        final JdbiFactory factory = new JdbiFactory();
        Jdbi db = newDatabase(factory, env, config.getDatabase(), "database");

        final UserDao userDao = db.onDemand(UserDao.class);

        // UserResource
        val userService = new UserService(userDao);
        userResource = new UserResource(userService);

        // HealthChecks
        HealthCheck dbHealthCheck = new DBHealthCheck("users");
        HealthCheck.Result healthCheckResult = dbHealthCheck.execute();
        if(healthCheckResult.isHealthy()) { log.info("DB is healthy"); } else { log.error("DB is unhealthy");}
    }

    /** Generates a new database pool. */
    private static Jdbi newDatabase(JdbiFactory jdbiFactory, Environment env,
                                    DataSourceFactory dataSourceFactory, String name) {
        val db = jdbiFactory.build(env, dataSourceFactory, name);
        db.installPlugin(new SqlObjectPlugin());
        return db;
    }
}
