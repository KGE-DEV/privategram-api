package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import com.garrettestrin.PrivateGram.data.PostDao;

import java.util.List;

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

    if(lower_limit > 0) {
      lower_limit = (lower_limit - 1) * 10;
    }
    return new PostResponse(true, null, postDao.getPaginatedPosts(lower_limit));
  }

  public PostResponse editPost(int postId, String postContent) {
    boolean wasPostEdited = postDao.editPost(postId, postContent);
    String postEditedMessage = wasPostEdited ? EDITED_POST_SUCCESS : EDITED_POST_FAIL;
    List<Post> updatedPost = postDao.getPost(postId);
    return new PostResponse(wasPostEdited, postEditedMessage, updatedPost);
  }

  public PostResponse deletePost(int postId) {
    boolean wasPostDeleted = postDao.deletePost(postId);
    String postDeletedMessage = wasPostDeleted ? DELETED_POST_SUCCESS : DELETED_POST_FAIL;
    return new PostResponse(wasPostDeleted, postDeletedMessage, null);
  }

}
