package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.data.PostDao;

public class PostService {

  private final PostDao postDao;

  private final String DELETED_POST_SUCCESS = "post was deleted";
  private final String DELETED_POST_FAIL = "post was not deleted";
  private final String EDITED_POST_SUCCESS = "post was edited";
  private final String EDITED_POST_FAIL = "post was not edited";

  public PostService(PostDao postDao) {
    this.postDao = postDao;
  }

  public PostResponse addPost(String postContent, String postImageUrl) {
//    when wp is no longer encoded emojis, we need to url encode the string for db insertion
    boolean wasPostAdded = postDao.addPost(postContent, postImageUrl);
    return new PostResponse(wasPostAdded, "Posted", null);
  }

  public PostResponse getAllPosts() {
    return new PostResponse(true, null, postDao.getAllPosts());
  }

  public PostResponse getPaginatedPosts(Integer lower_limit) {
    if(lower_limit == null) {
      lower_limit = 0;
    } else {
      lower_limit = lower_limit * 10;
    }
    return new PostResponse(true, null, postDao.getPaginatedPosts(lower_limit));
  }
}
