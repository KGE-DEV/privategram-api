package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Event;
import com.garrettestrin.PrivateGram.api.ApiObjects.EventResponse;
import com.garrettestrin.PrivateGram.data.EventDao;
import com.vdurmont.emoji.EmojiParser;

public class EventService {

  private final EventDao eventDao;

  public EventService(EventDao eventDao) {
    this.eventDao = eventDao;
  }

  public void saveEvent(Event event) {
    String meta = EmojiParser.parseToAliases(event.getMeta());
    eventDao.saveEvent(event.getUserId(), event.getEvent(), event.getPage(), meta);
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
