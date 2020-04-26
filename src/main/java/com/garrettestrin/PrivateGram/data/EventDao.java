package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Event;
import java.util.List;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface EventDao extends SqlObject {

  @SqlUpdate("INSERT INTO "
          + "`user_events` (`user_id`, `event`, `page`, `meta`) "
          + "VALUES (:user_id, :event, :page, :meta);")
  void saveEvent(@Bind("user_id") int userId, @Bind("event") String event, @Bind("page") String page, @Bind("meta") String meta);

  @SqlQuery("SELECT ue.id, ue.event, ue.user_id, ue.page, ue.meta, u.name FROM user_events as ue "
          + "INNER JOIN users as u on ue.user_id = u.id "
          + "WHERE event = :event "
          + "ORDER BY id DESC LIMIT 500")
  @RegisterBeanMapper(Event.class)
  List<Event> getEventsByEventType(@Bind("event") String event);

  @SqlQuery("SELECT ue.id, ue.event, ue.user_id, ue.page, ue.meta, u.name FROM user_events as ue "
          + "INNER JOIN users as u on ue.user_id = u.id "
          + "WHERE user_id = :user_id "
          + "ORDER BY id DESC LIMIT 500")
  @RegisterBeanMapper(Event.class)
  List<Event> getEventsByUser(@Bind("user_id") int user_id);

  @SqlQuery("SELECT ue.id, ue.event, ue.user_id, ue.page, ue.meta, u.name FROM user_events as ue "
          + "INNER JOIN users as u on ue.user_id = u.id "
          + "ORDER BY id DESC LIMIT 500")
  @RegisterBeanMapper(Event.class)
  List<Event> getLatestEvents();
}
