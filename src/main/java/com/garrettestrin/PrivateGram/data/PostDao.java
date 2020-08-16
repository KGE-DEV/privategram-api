package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PostDao extends SqlObject {
  @Deprecated
  @SqlUpdate("INSERT INTO "
          + "`posts` (`post_content`, `post_image_url`) "
          +   "VALUES (:post_content, :post_image_url);")
  boolean addPost(@Bind("post_content") String post_content, @Bind("post_image_url") String post_image_url);

  /**
   * Insert new post in database
   * @param post_content
   * @param post_image_url
   * @param is_private
   * @return boolean representing successful insertion
   */
  @SqlUpdate("INSERT INTO "
          + "`posts` (`post_content`, `post_image_url`, `private`) "
          + "VALUES (:post_content, :post_image_url, :is_private);")
  boolean addPost(@Bind("post_content") String post_content, @Bind("post_image_url") String post_image_url, @Bind("is_private") boolean is_private);

  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE id = :post_id")
  @RegisterBeanMapper(Post.class)
  List<Post> getPost(@Bind("post_id") int postId);

  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE active = 1 "
          + "Order by id DESC ")
  @RegisterBeanMapper(Post.class)
  List<Post> getAllPosts();

  /**
   * Returns paginated list of posts
   * that are active and correspond to the users clearance level
   * @param lower_limit
   * @param isAdmin
   * @return List<Post>
   */
  @SqlQuery("SELECT * "
          + "FROM `posts` "
          + "WHERE active = 1 and private <= :is_admin "
          + "Order by id DESC "
          + "LIMIT :lower_limit, 10")
  @RegisterBeanMapper(Post.class)
  List<Post> getPaginatedPosts(@Bind("lower_limit") int lower_limit, @Bind("is_admin") boolean isAdmin);

  @SqlUpdate("UPDATE `posts` "
          + "SET post_content = :post_content "
          + "WHERE id = :post_id")
  boolean editPost(@Bind("post_id") int postId, @Bind("post_content") String postContent);

  @SqlUpdate("UPDATE `posts` "
          + "SET active = 0 "
          + "WHERE id = :post_id")
  boolean deletePost(@Bind("post_id") int postId);

  @SqlQuery("SELECT COUNT(*) FROM posts "
          + "WHERE active = 1")
  int postCount();
}
