package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.data.Cache;
import com.garrettestrin.PrivateGram.data.CommentDao;
import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import com.vdurmont.emoji.EmojiParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentService {

  private final CommentDao commentDao;
  private final Cache cache;

  private final String DELETED_MESSAGE_SUCCESS = "comment was deleted";
  private final String DELETED_MESSAGE_FAIL = "comment was not deleted";
  private final String EDITED_MESSAGE_SUCCESS = "comment was edited";
  private final String EDITED_MESSAGE_FAIL = "comment was not edited";

  public CommentService(CommentDao commentDao, Cache cache) {
    this.commentDao = commentDao;
    this.cache = cache;
  }


  public CommentResponse postComment(String comment, int postId, int userId) {
    cache.removeCommentCache(cache.COMMENTS_FOR_ + postId);
    cache.removeCommentCache(cache.PREVIEW_COMMENTS_FOR_ + postId);
    String encodedComment  = EmojiParser.parseToAliases(comment);
    boolean wasCommentPosted = commentDao.postComment(postId, encodedComment, userId);
    return new CommentResponse(wasCommentPosted, null, null);
  }

  public CommentResponse getCommentsPreview(int post_id) {
    List<Comment> comments = parseCommentsForEmojis(commentDao.getCommentsPreview(post_id));
    return new CommentResponse(true, null, comments);
  }

  public CommentResponse getAllComments(int postId) throws IOException {
    // check cache for data
    String cachedComments = cache.getComments(cache.COMMENTS_FOR_ + postId);
    // if data is cached
    if (null != cachedComments) {
      List<Comment> comments = cache.decode(cachedComments, List.class);
      return new CommentResponse(true, null, comments);
    }
    List<Comment> comments = parseCommentsForEmojis(commentDao.getAllComments(postId));
    cache.setComments(cache.COMMENTS_FOR_ + postId, cache.encode(comments));
    return new CommentResponse(true, null, comments);
  }

  private List<Comment> parseCommentsForEmojis(List<Comment> comments) {
    List<Comment> parsedComments = new ArrayList<>();
    for(int i = 0; i < comments.size();i++) {
      Comment comment = comments.get(i);
      Comment tempComment = Comment.builder().comment(EmojiParser.parseToUnicode(comment.getComment())).id(comment.getId()).name(comment.getName()).post_id(comment.post_id).build();
      parsedComments.add(i, tempComment);
    }
    return parsedComments;
  }

  public CommentResponse editComment(int comment_id, String comment) {
    boolean wasCommentUpdated = commentDao.editComment(comment_id, comment);
    String message = wasCommentUpdated ? EDITED_MESSAGE_SUCCESS : EDITED_MESSAGE_FAIL;
    List<Comment> updatedComment = commentDao.getComment(comment_id);
    return new CommentResponse(wasCommentUpdated, message, updatedComment);
  }

  public CommentResponse deleteComment(int comment_id) {
      boolean wasCommentDeleted = commentDao.deleteComment(comment_id);
      String message = wasCommentDeleted ? DELETED_MESSAGE_SUCCESS : DELETED_MESSAGE_FAIL;
      return new CommentResponse(wasCommentDeleted, message, null);
  }
}
