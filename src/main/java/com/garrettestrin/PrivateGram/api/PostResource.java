package com.garrettestrin.PrivateGram.api;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;


import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Post;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostCountResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.PostService;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

  private final PostService postService;
  private final String AUTH_COOKIE = "api_auth";

  public PostResource(PostService postService) {
    this.postService = postService;
  }

  /**
   * @param fileInputStream
   * @param authenticatedUser
   * @param caption
   * @return PostResponse
   */
  @Deprecated
  @POST
  @Path("/add")
  @Consumes(MULTIPART_FORM_DATA)
  public PostResponse addPost(
          @FormDataParam("caption") String caption,
          @FormDataParam("file") final InputStream fileInputStream,
          @FormDataParam("file") final FormDataContentDisposition contentDispositionHeader,
          @FormDataParam("name") String name,
          @FormDataParam("type") String type,
          @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return postService.addPost(caption, fileInputStream, name, type);
  }

  /**
   * @param fileInputStream
   * @param authenticatedUser
   * @param caption
   * @param isPrivate
   * @return PostResponse
   */
  @POST
  @Path("/v2/add")
  @Consumes(MULTIPART_FORM_DATA)
  public PostResponse addPost(
          @FormDataParam("caption") String caption,
          @FormDataParam("file") final InputStream fileInputStream,
          @FormDataParam("file") final FormDataContentDisposition contentDispositionHeader,
          @FormDataParam("name") String name,
          @FormDataParam("type") String type,
          @FormDataParam("isPrivate") boolean isPrivate,
          @FormDataParam("height") int height,
          @FormDataParam("width") int width,
          @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return postService.addPost(caption, fileInputStream, name, type, isPrivate, height, width);
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
  public PostResponse getPaginatedPosts(@PathParam("page") Integer page, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return postService.getPaginatedPosts(page, authenticatedUser.isAdmin());
  }

  /**
   * @param pageId
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("get/{pageId}")
  public PostResponse getIndividualPost(@PathParam("pageId") Integer pageId, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getIndvidualPost(pageId, authenticatedUser.isAdmin());
  }

  @PUT
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
