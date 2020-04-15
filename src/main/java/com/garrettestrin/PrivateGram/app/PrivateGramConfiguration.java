package com.garrettestrin.PrivateGram.app;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class PrivateGramConfiguration extends Configuration {

    @NotEmpty
    private String salt;
    @NotEmpty
    private String secretKey;
    @NotEmpty
    private String authToken;

    @NotEmpty
    private String emailUser;
    @NotEmpty
    private String emailHost;
    @NotEmpty
    private String emailPassword;

    @Valid
    @NotNull
    private DataSourceFactory database;

    @Valid
    @NotNull
    private String wpUrl;
}
