package com.garrettestrin.sample.app;

import io.dropwizard.Application;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.jbosslog.JBossLog;
import lombok.val;

@JBossLog
public class SampleApplication extends Application<SampleConfiguration> {

    @Override
    public String getName() {
        return "Sample";
    }

    @Override
    public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
//        bootstrap.setConfigurationSourceProvider(
//                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
//                        new EnvironmentVariableSubstitutor()));
        ConfigurationSourceProvider sourceProvider = new SubstitutingSourceProvider(
                new ResourceConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false));
        bootstrap.setConfigurationSourceProvider(sourceProvider);
    }

    @Override
    public void run(SampleConfiguration configuration,
                    Environment environment) {
        val deps = new DependencyManager(configuration, environment);

        // Register resources
        log.info("Registering Resources.");
        environment.jersey().register(deps.sampleResource);
    }

}
