package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Comment;

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
  public Comment registerUser() {

    return new Comment("This is a comment");
  }
}
