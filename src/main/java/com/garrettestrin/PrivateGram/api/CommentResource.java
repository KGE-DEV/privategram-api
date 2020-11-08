package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Comment;
import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.CommentService;
import io.dropwizard.jersey.PATCH;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {
  private final CommentService commentService;
  private final String AUTH_COOKIE = "api_auth";

  public CommentResource(CommentService commentService) {
    this.commentService = commentService;
  }
  // TODO: JAVADOC
  // TODO: add error handling
  @POST
  @Path("/add")
  @Timed
  public CommentResponse addComment(Comment c, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return commentService.postComment(c.comment, c.postId, authenticatedUser.getUserId());
  }

  @GET
  @Path("get/all")
  @Timed
  public CommentResponse getAllComments(@QueryParam("post_id") int post_id, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return commentService.getAllComments(post_id);
  }

  @GET
  @Path("get/preview")
  @Timed
  public CommentResponse getCommentsPreview(@QueryParam("post_id") int post_id, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return commentService.getCommentsPreview(post_id);
  }

  @PATCH
  @Path("edit")
  @Timed
  public CommentResponse editComment(Comment c, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return commentService.editComment(c.commentId, c.comment);
  }

  @DELETE
  @Path("delete")
  @Timed
  public CommentResponse deleteComment(@QueryParam("comment_id") int comment_id, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return commentService.deleteComment(comment_id);
  }
}
