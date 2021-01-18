package com.garrettestrin.PrivateGram.app;

import io.dropwizard.Application;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.jbosslog.JBossLog;
import lombok.val;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@JBossLog
public class PrivateGramApplication extends Application<PrivateGramConfiguration> {

    @Override
    public String getName() {
        return "PrivateGram";
    }

    @Override
    public void initialize(Bootstrap<PrivateGramConfiguration> bootstrap) {
        ConfigurationSourceProvider sourceProvider = new SubstitutingSourceProvider(
                new ResourceConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false));
        bootstrap.setConfigurationSourceProvider(sourceProvider);
    }

    @Override
    public void run(PrivateGramConfiguration configuration,
                    Environment environment) {
        val deps = new DependencyManager(configuration, environment);

        // Register resources
        log.info("Registering Resources.");
        environment.jersey().register(deps.userResource);
        environment.jersey().register(deps.commentResource);
        environment.jersey().register(deps.postResource);
        environment.jersey().register(deps.eventResource);
        environment.jersey().register(deps.cacheResource);

        environment.jersey().register(deps.authenticatedUserConverterProvider);
        environment.jersey().register(deps.unauthorizedExceptionMapper);
        environment.jersey().register(deps.serverErrorExceptionMapper);

        environment.jersey().register(MultiPartFeature.class);

        Cors.insecure(environment);

        // Register Cron Jobs on application start
//        SendDailyUpdateEmail sendDailyUpdateEmail = new SendDailyUpdateEmail(deps.bizUtilities);
//        CronUtilities cronUtilities = new CronUtilities(sendDailyUpdateEmail);
//        cronUtilities.scheduleDailyEmailUpdate();
    }

}
