package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.AddComment;
import com.garrettestrin.PrivateGram.api.ApiObjects.Comment;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {
  // TODO: JAVADOC
  // TODO: add error handling
  @POST
  @Path("/add")
  @Timed
  public Comment addComment(AddComment c, @CookieParam("elsie_gram_auth") AuthenticatedUser authenticatedUser) throws Exception{
    try {
      return new Comment(c.comment + " " + c.userId + " " + c.postId + ". " + authenticatedUser.getValue());
    } catch (Exception ex) {
      return new Comment("There was an error");
    }
  }
}


