package com.garrettestrin.PrivateGram.api;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;


import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Post;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostCountResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.RotateImage;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.PostService;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.dropwizard.jersey.PATCH;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.jvnet.hk2.annotations.Optional;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

  private final PostService postService;
  private final String AUTH_COOKIE = "api_auth";

  public PostResource(PostService postService) {
    this.postService = postService;
  }

  /**
   * @param authenticatedUser
   * @param caption
   * @param isPrivate
   * @return PostResponse
   */
  @POST
  @Path("/v3/add")
  @Consumes(MULTIPART_FORM_DATA)
  public PostResponse addPost(
          @FormDataParam("caption") String caption,
          @FormDataParam("isPrivate") boolean isPrivate,
          @FormDataParam("fileData") JSONArray filesData,
          FormDataMultiPart multiPart,
          @Optional @FormDataParam("siteKey") String siteKey,
          @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return postService.handleMultiPost(caption, isPrivate, filesData, multiPart, siteKey);
  }

  /**
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("/get/all")
  @Timed
  public PostResponse getAllPosts(@QueryParam("siteKey") String siteKey,
                                  @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getAllPosts(siteKey);
  }


  /**
   * @param page
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("get/paginated/{page}")
  public PostResponse getPaginatedPosts(@PathParam("page") Integer page,
                                        @QueryParam("siteKey") String siteKey,
                                        @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    return postService.getPaginatedPosts(page, authenticatedUser.isAdmin(), siteKey);
  }

  /**
   * @param pageId
   * @param authenticatedUser
   * @return PostResponse
   */
  @GET
  @Path("get/{pageId}")
  public PostResponse getIndividualPost(@PathParam("pageId") Integer pageId,
                                        @QueryParam("siteKey") String siteKey,
                                        @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.getIndividualPost(pageId, authenticatedUser.isAdmin());
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
  public PostCountResponse postCount(@QueryParam("siteKey") String siteKey,
          @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
    return postService.postCount(authenticatedUser.isAdmin(), siteKey);
  }

  @POST
  @Path("/rotate-image")
  @Timed
  public PostResponse rotateImage(RotateImage image,
                                  @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) throws IOException {
    if (!authenticatedUser.isAdmin) {
      return PostResponse.builder().message("Access Denied").build();
    }
    return postService.rotateImage(image);
  }
}
