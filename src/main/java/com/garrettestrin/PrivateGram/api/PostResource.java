package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Post;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.PostService;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

  private final PostService postService;
  private final String AUTH_COOKIE = "elsie_gram_auth";

  public PostResource(PostService postService) {
    this.postService = postService;
  }
  // TODO: JAVADOC
  // TODO: add error handling
  @POST
  @Path("/add")
  @Timed
  public PostResponse addComment(Post p, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.addPost(p.postContent, p.postImageUrl);
  }

  @GET
  @Path("/get/all")
  @Timed
  public PostResponse getAllPosts(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getAllPosts();
  }

  @GET
  @Path("get/paginated")
  public PostResponse getPaginatedPosts(@QueryParam("page") Integer page, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getPaginatedPosts(page);
  }
}
