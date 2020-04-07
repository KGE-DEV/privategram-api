package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.biz.EventService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/event")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

  private final EventService eventService;

  private final String AUTH_COOKIE = "elsie_gram_auth";

  public EventResource(EventService eventService) {
    this.eventService = eventService;
  }

  @POST
  @Timed
  public void trackEvent(Event event) {
    String eventName = event.getEvent();
    eventService.saveEvent(event);
  }
}
