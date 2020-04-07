package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jersey.repackaged.com.google.common.base.Optional;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

  private final String AUTH_COOKIE = "elsie_gram_auth";

  @POST
  @Path("/")
  @Timed
  public void trackEvent(Event event, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    String eventName = event.getEvent();
  }
}
