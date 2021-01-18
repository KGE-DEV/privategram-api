package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.data.Cache;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/cache")
public class CacheResource {
    private final Cache cache;
    private final String AUTH_COOKIE = "api_auth";

    public CacheResource(Cache cache) {
        this.cache = cache;
    }

    @POST
    @Path("/clear")
    @Timed
    public void clearCache(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {

        cache.clearPostCache();
        cache.clearCommentCache();
    }
}
