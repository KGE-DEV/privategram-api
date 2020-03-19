package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface CommentDao extends SqlObject{
  // TODO: JAVADOC
  @SqlUpdate("INSERT INTO "
          + "`comments` (`post_id`, `comment`, `user_id`) "
          +   "VALUES (:post_id, :comment, :user_id) ")
  boolean postComment(@Bind("post_id") int post_id, @Bind("comment") String comment, @Bind("user_id") String user_id);

//  TODO: JAVADOC
  @SqlQuery("SELECT * "
            + "FROM `comments` "
            + "WHERE post_id = :post_id")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getAllComments(@Bind("post_id") int post_id);
}

