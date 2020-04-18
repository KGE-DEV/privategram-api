package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
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
          +   "VALUES (:post_id, :comment, :user_id);")
  boolean postComment(@Bind("post_id") int post_id, @Bind("comment") String comment, @Bind("user_id") int user_id);

  // TODO: JAVADOC
  @SqlQuery("SELECT * "
            + "FROM `comments` "
            + "WHERE id = :comment_id LIMIT 1")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getComment(@Bind("comment_id") int comment_id);

//  TODO: JAVADOC
@SqlQuery("SELECT c.id, c.post_id, c.comment, u.name FROM comments as c "
        + "INNER JOIN users as u on c.user_id = u.id "
        + "WHERE post_id = :post_id and active = 1 ")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getAllComments(@Bind("post_id") int post_id);

  //  TODO: JAVADOC
  @SqlQuery("SELECT c.id, c.post_id, c.comment, u.name FROM comments as c "
          + "INNER JOIN users as u on c.user_id = u.id "
          + "WHERE post_id = :post_id and c.active = 1 "
          + "LIMIT 3")
  @RegisterBeanMapper(Comment.class)
  List<Comment> getCommentsPreview(@Bind("post_id") int post_id);

  // TODO: JAVADOC
  @SqlUpdate("UPDATE `comments` "
            + "SET comment = :comment "
            + "WHERE id = :comment_id")
  boolean editComment(@Bind("comment_id") int comment_id, @Bind("comment") String comment);

  //  TODO: JAVADOC
  @SqlUpdate("UPDATE `comments` "
          + "SET active = 0 "
          + "WHERE id = :comment_id")
  boolean deleteComment(@Bind("comment_id") int comment_id);
}

