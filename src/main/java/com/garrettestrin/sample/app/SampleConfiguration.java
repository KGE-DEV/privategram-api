package com.garrettestrin.sample.app;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SampleConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database;
}
