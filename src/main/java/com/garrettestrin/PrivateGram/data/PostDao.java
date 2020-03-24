package com.garrettestrin.PrivateGram.data;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PostDao extends SqlObject {
  // TODO: JAVADOC
  @SqlUpdate("INSERT INTO "
          + "`posts` (`post_content`, `post_image_url`) "
          +   "VALUES (:post_content, :post_image_url);")
  boolean addPost(@Bind("post_content") String post_content, @Bind("post_image_url") String post_image_url);
}
