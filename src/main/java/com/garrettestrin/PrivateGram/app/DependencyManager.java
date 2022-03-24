package com.garrettestrin.PrivateGram.app;

import com.codahale.metrics.health.HealthCheck;
import com.garrettestrin.PrivateGram.api.CacheResource;
import com.garrettestrin.PrivateGram.api.CommentResource;
import com.garrettestrin.PrivateGram.api.EventResource;
import com.garrettestrin.PrivateGram.api.PostResource;
import com.garrettestrin.PrivateGram.api.UserResource;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUserConverterProvider;
import com.garrettestrin.PrivateGram.app.Auth.UnauthorizedExceptionMapper;
import com.garrettestrin.PrivateGram.app.Config.AWSConfig;
import com.garrettestrin.PrivateGram.biz.BizUtilities;
import com.garrettestrin.PrivateGram.biz.CommentService;
import com.garrettestrin.PrivateGram.biz.EventService;
import com.garrettestrin.PrivateGram.biz.PostService;
import com.garrettestrin.PrivateGram.biz.UserService;
import com.garrettestrin.PrivateGram.data.Cache;
import com.garrettestrin.PrivateGram.data.CommentDao;
import com.garrettestrin.PrivateGram.data.EventDao;
import com.garrettestrin.PrivateGram.data.PostDao;
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
    public final CommentResource commentResource;
    public final PostResource postResource;
    public final EventResource eventResource;
    public final CacheResource cacheResource;
    public final AuthenticatedUserConverterProvider authenticatedUserConverterProvider;
    public final UnauthorizedExceptionMapper unauthorizedExceptionMapper;
    public final ServerErrorExceptionMapper serverErrorExceptionMapper;
    public final BizUtilities bizUtilities;


    DependencyManager(PrivateGramConfiguration config, Environment env) {
        log.info("Initializing database pool...");
        // This creates a managed database and creates a JdbiHealthCheck
        final JdbiFactory factory = new JdbiFactory();
        log.info("DB info: " + config.getDatabase().getUrl() + " " + config.getDatabase().getPassword() + " " + config.getDatabase().getUser());
        Jdbi db = newDatabase(factory, env, config.getDatabase(), "database");

        // create in memory cache
        Cache cache = new Cache();

        final Auth auth = new Auth(config);
        // aws
        AWSConfig awsConfig;

        // aws
        awsConfig = config.getAwsConfig();

        bizUtilities = new BizUtilities(config);

        // UserResource
         final UserDao userDao = db.onDemand(UserDao.class);
         val userService = new UserService(userDao, auth, config);
        userResource = new UserResource(auth, userService);
        // CommentResource
        final CommentDao commentDao = db.onDemand(CommentDao.class);
        val commentService = new CommentService(commentDao, cache, bizUtilities);
        commentResource = new CommentResource(commentService);

        // PostResource
        final PostDao postDao = db.onDemand(PostDao.class);
        val postService = new PostService(postDao, awsConfig, config.getTinypngKey(), config, userDao, cache);
        postResource = new PostResource(postService);

        // EventResource
        final EventDao eventDao = db.onDemand(EventDao.class);
        val eventService = new EventService(eventDao);
        eventResource = new EventResource(eventService, userService);

        // CacheResource
        cacheResource = new CacheResource(cache);

        authenticatedUserConverterProvider = new AuthenticatedUserConverterProvider(config, userDao);
        unauthorizedExceptionMapper = new UnauthorizedExceptionMapper();
        serverErrorExceptionMapper = new ServerErrorExceptionMapper(config);

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
