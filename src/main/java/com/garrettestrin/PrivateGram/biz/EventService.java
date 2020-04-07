package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.data.EventDao;

public class EventService {

  private final EventDao eventDao;

  public EventService(EventDao eventDao) {
    this.eventDao = eventDao;
  }

  public void saveEvent(Event event) {
    eventDao.saveEvent(event.getUserId(), event.getEvent(), event.getPage());
  }
}
