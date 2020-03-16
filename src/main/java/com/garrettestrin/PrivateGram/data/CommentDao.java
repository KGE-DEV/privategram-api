package com.garrettestrin.PrivateGram.data;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CommentDao extends SqlObject{
  // TODO: JAVADOC
  @SqlUpdate("INSERT INTO "
          + "`comments` (`post_id`, `comment`, `user_id`) "
          +   "VALUES (:post_id, :comment, :user_id) ")
  boolean postComment(@Bind("post_id") int post_id, @Bind("comment") String comment, @Bind("user_id") String user_id);
}

