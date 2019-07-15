package com.garrettestrin.PrivateGram.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrivateGramConfiguration extends Configuration {

    @NotEmpty
    private String salt;
    @NotEmpty
    private String secretKey;

    @Valid
    @NotNull
    private DataSourceFactory database;
}
