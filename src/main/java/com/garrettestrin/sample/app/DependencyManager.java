package com.garrettestrin.sample.app;

import com.garrettestrin.sample.api.SampleResource;
import com.garrettestrin.sample.biz.SampleService;
import com.garrettestrin.sample.data.SampleDao;
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
    public final SampleResource sampleResource;

    DependencyManager(SampleConfiguration config, Environment env) {
        log.info("Initializing database pool...");
        // This creates a managed database and creates a JdbiHealthCheck
        final JdbiFactory factory = new JdbiFactory();
        Jdbi sampleDb = newDatabase(factory, env, config.getDatabase(), "database");

        final SampleDao sampleDao = sampleDb.onDemand(SampleDao.class);

        // SampleResource
        val sampleService = new SampleService(sampleDao);
        sampleResource = new SampleResource(sampleService);
    }

    /** Generates a new database pool. */
    private static Jdbi newDatabase(JdbiFactory jdbiFactory, Environment env,
                                    DataSourceFactory dataSourceFactory, String name) {
        val db = jdbiFactory.build(env, dataSourceFactory, name);
        db.installPlugin(new SqlObjectPlugin());
        return db;
    }
}
