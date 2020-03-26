package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PostDao extends SqlObject {
  // TODO: JAVADOC
  @SqlUpdate("INSERT INTO "
          + "`posts` (`post_content`, `post_image_url`) "
          +   "VALUES (:post_content, :post_image_url);")
  boolean addPost(@Bind("post_content") String post_content, @Bind("post_image_url") String post_image_url);

  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE id = :post_id")
  @RegisterBeanMapper(Post.class)
  List<Post> getPost(@Bind("post_id") int postId);

  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE active = 1")
  @RegisterBeanMapper(Post.class)
  List<Post> getAllPosts();

  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE active = 1 "
          + "LIMIT :lower_limit, 10")
  @RegisterBeanMapper(Post.class)
  List<Post> getPaginatedPosts(@Bind("lower_limit") int lower_limit);

  @SqlUpdate("UPDATE `posts` "
          + "SET post_content = :post_content "
          + "WHERE id = :post_id")
  boolean editPost(@Bind("post_id") int postId, @Bind("post_content") String postContent);

  @SqlUpdate("UPDATE `posts` "
          + "SET active = 0 "
          + "WHERE id = :post_id")
  boolean deletePost(@Bind("post_id") int postId);
}
