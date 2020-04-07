package com.garrettestrin.PrivateGram.data;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface EventDao extends SqlObject {

  @SqlUpdate("INSERT INTO "
          + "`user_events` (`user_id`, `event`, `page`) "
          + "VALUES (:user_id, :event, :page);")
  void saveEvent(@Bind("user_id") int userId, @Bind("event") String event, @Bind("page") String page);

}
