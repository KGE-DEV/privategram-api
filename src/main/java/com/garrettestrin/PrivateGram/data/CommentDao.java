package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import org.hibernate.annotations.SQLDelete;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface CommentDao extends SqlObject {
  // TODO: JAVADOC
  @SqlUpdate("INSERT INTO "
          + "`comments` (`post_id`, `comment`, `user_id`) "
          +   "VALUES (:post_id, :comment, :user_id) ")
  boolean postComment(@Bind("post_id") int post_id, @Bind("comment") String comment, @Bind("user_id") String user_id);

//  TODO: JAVADOC
  @SqlQuery("SELECT * "
            + "FROM `comments` "
            + "WHERE post_id = :post_id AND active = 1")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getAllComments(@Bind("post_id") int post_id);

  //  TODO: JAVADOC
  @SqlQuery("SELECT * "
          + "FROM `comments` "
          + "WHERE post_id = :post_id AND active = 1"
          + "LIMIT 3")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getCommentsPreview(@Bind("post_id") int post_id);

  //  TODO: JAVADOC
  @SqlUpdate("UPDATE `comments` "
          + "SET active = 0 "
          + "WHERE id = :comment_id")
  boolean deleteComment(@Bind("comment_id") int comment_id);
}

