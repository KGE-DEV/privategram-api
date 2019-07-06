package com.garrettestrin.PrivateGram.app;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrivateGramConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database;
}
