package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.data.CommentDao;
import com.garrettestrin.PrivateGram.data.DataObjects.Comment;

import java.util.List;

public class CommentService {

  private final CommentDao commentDao;

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }


  public CommentResponse postComment(String comment, int postId, String userId) {
    boolean wasCommentPosted = commentDao.postComment(postId, comment, userId);
    if(wasCommentPosted) {
      return new CommentResponse(true, null, null);
    }
    return new CommentResponse(false, null, null);
  }

  public CommentResponse getAllComments(int post_id) {
    List<Comment> comments = commentDao.getAllComments(post_id);
    return new CommentResponse(true, null, comments);
  }
}
