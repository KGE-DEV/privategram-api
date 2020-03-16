package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.data.CommentDao;

public class CommentService {

  private final CommentDao commentDao;

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }
  public CommentResponse postComment(String comment, int postId, String userId) {
    boolean wasCommentPosted = commentDao.postComment(postId, comment, userId);
    if(wasCommentPosted) {
      return new CommentResponse(true, null);
    }
    return new CommentResponse(false, null);
  }
}
