package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.api.ApiObjects.EventResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.EventService;
import com.garrettestrin.PrivateGram.biz.UserService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

  private final EventService eventService;
  private final UserService userService;
  private final String AUTH_COOKIE = "api_auth";

  public EventResource(EventService eventService, UserService userService) {
    this.eventService = eventService;
    this.userService = userService;
  }

  @POST
  @Timed
  public void trackEvent(Event event) {
    eventService.saveEvent(event);
  }

  @GET
  @Path("/by/event")
  @Timed
  public EventResponse getEventsByEventType(@QueryParam("event") String event, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    String role = userService.getUserRole(authenticatedUser.getUserId());
    if(!role.equals("admin")) {
      return EventResponse.builder().success(false).build();
    }
    return eventService.getEventsByEventType(event);
  }

  @GET
  @Path("/by/user")
  @Timed
  public EventResponse getEventsByUser(@QueryParam("userId") int userId, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    String role = userService.getUserRole(authenticatedUser.getUserId());
    if(!role.equals("admin")) {
      return EventResponse.builder().success(false).build();
    }
    return eventService.getEventsByUser(userId);
  }
}
