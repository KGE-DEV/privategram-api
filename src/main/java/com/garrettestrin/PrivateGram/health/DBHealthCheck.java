package com.garrettestrin.PrivateGram.health;

import com.codahale.metrics.health.HealthCheck;

public class DBHealthCheck extends HealthCheck{

    private final String table;

    public DBHealthCheck(String table) {
        this.table = table;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(table, "users");
        if (!saying.contains("users")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
