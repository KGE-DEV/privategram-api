package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Post;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostCountResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.PostService;
import io.dropwizard.jersey.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Param;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

  private final PostService postService;
  private final String AUTH_COOKIE = "elsie_gram_auth";

  public PostResource(PostService postService) {
    this.postService = postService;
  }

  /**
   * @param post
   * @param authenticatedUser
   * @return PostResponse
   */
  @POST
  @Path("/add")
  @Timed
  public PostResponse addPost(Post post, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.addPost(post.postContent, post.postImageUrl);
  }

  /**
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("/get/all")
  @Timed
  public PostResponse getAllPosts(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getAllPosts();
  }


  /**
   * @param page
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("get/paginated/{page}")
  public PostResponse getPaginatedPosts(@PathParam("page") Integer page, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getPaginatedPosts(page);
  }

  @PATCH
  @Path("/edit")
  @Timed
  public PostResponse editPost(Post post, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.editPost(post.postId, post.postContent);
  }

  @DELETE
  @Path("/delete")
  @Timed
  public PostResponse deletePost(Post p, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.deletePost(p.postId);
  }

  @GET
  @Path("/count")
  @Timed
  public PostCountResponse postCount(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.postCount();
  }
}
