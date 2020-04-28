package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.api.ApiObjects.EventResponse;
import com.garrettestrin.PrivateGram.data.EventDao;

public class EventService {

  private final EventDao eventDao;

  public EventService(EventDao eventDao) {
    this.eventDao = eventDao;
  }

  public void saveEvent(Event event) {
    eventDao.saveEvent(event.getUserId(), event.getEvent(), event.getPage(), event.getMeta());
  }

  public EventResponse getEventsByEventType(String event) {
    return EventResponse.builder().success(true).events(eventDao.getEventsByEventType(event)).build();
  }

  public EventResponse getEventsByUser(int userId) {
    return EventResponse.builder().success(true).events(eventDao.getEventsByUser(userId)).build();
  }

  public EventResponse getLatestEvents() {
    return EventResponse.builder().success(true).events(eventDao.getLatestEvents()).build();
  }
}
