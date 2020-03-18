package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Comment;
import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.app.Auth.UnauthorizedException;
import com.garrettestrin.PrivateGram.biz.CommentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {
  private final CommentService commentService;

  public CommentResource(CommentService commentService) {
    this.commentService = commentService;
  }
  // TODO: JAVADOC
  // TODO: add error handling
  @POST
  @Path("/add")
  @Timed
  public CommentResponse addComment(Comment c, @CookieParam("elsie_gram_auth") AuthenticatedUser authenticatedUser) {
    return commentService.postComment(c.comment, c.postId, authenticatedUser.getUserId());
  }

  @GET
  @Path("get/all")
  @Timed
  public CommentResponse getAllComments(@QueryParam("post_id") int post_id, @CookieParam("elsie_gram_auth") AuthenticatedUser authenticatedUser) {
    return commentService.getAllComments(post_id);
  }
}
