package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.data.CommentDao;
import com.garrettestrin.PrivateGram.data.DataObjects.Comment;

import java.util.List;

public class CommentService {

  private final CommentDao commentDao;

  private final String DELETED_MESSAGE_SUCCESS = "comment was deleted";
  private final String DELETED_MESSASGE_FAIL = "comment was not deleted";
  private final String EDITED_MESSAGE_SUCCESS = "comment was edited";
  private final String EDITED_MESSAGE_FAIL = "comment was not edited";

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }


  public CommentResponse postComment(String comment, int postId, String userId) {
    boolean wasCommentPosted = commentDao.postComment(postId, comment, userId);
    return new CommentResponse(wasCommentPosted, null, null);
  }

  public CommentResponse getAllComments(int post_id) {
    List<Comment> comments = commentDao.getAllComments(post_id);
    return new CommentResponse(true, null, comments);
  }

  public CommentResponse getCommentsPreview(int post_id) {
    List<Comment> comments = commentDao.getCommentsPreview(post_id);
    return new CommentResponse(true, null, comments);
  }

  public CommentResponse editComment(int comment_id, String comment) {
    boolean wasCommentUpdated = commentDao.editComment(comment_id, comment);
    String message = wasCommentUpdated ? EDITED_MESSAGE_SUCCESS : EDITED_MESSAGE_FAIL;
    List<Comment> updatedComment = commentDao.getComment(comment_id);
    return new CommentResponse(wasCommentUpdated, message, updatedComment);
  }

  public CommentResponse deleteComment(int comment_id) {
      boolean wasCommentDeleted = commentDao.deleteComment(comment_id);
      String message = wasCommentDeleted ? DELETED_MESSAGE_SUCCESS : DELETED_MESSASGE_FAIL;
      return new CommentResponse(wasCommentDeleted, message, null);
  }
}
