package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.*;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.UserService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserService userService;
    private Auth auth;

    private final String AUTH_COOKIE = "api_auth";

    public UserResource(Auth auth, UserService userService) {
        this.userService = userService;
        this.auth = auth;
    }

    @POST
    @Path("/create/token")
    public JWTToken getToken(@QueryParam("secret") String secret, @QueryParam("user_id") int userId) {
        if(!secret.equals(auth.SECRET_KEY)) {
            return new JWTToken(null);
        }
        return new JWTToken(auth.createJWT(userId, "garrett.estrin.com", AUTH_COOKIE, -1));
    }

    @POST
    @Path("/add")
    @Timed
    public UserResponse addUser(@QueryParam("email") String email,
                            @QueryParam("name") String name,
                            @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        if(role != "admin") {
            return UserResponse.builder().success(false).build();
        }
        return userService.addUser(email, name);
    }

    // TODO: JAVADOC
    // TODO: add error handling
    @POST
    @Path("/login")
    @Timed
    public UserResponse login(@QueryParam("email") String email,
                                @QueryParam("password") String password,
                                 @Context HttpServletResponse response,
                                 @Context HttpServletRequest request) throws IOException {

        return userService.loginUser(email, password, response, request);
    }

    // TODO: JAVADOC
    // TODO: add error handling
    @POST
    @Path("/password/request/reset")
    @Timed
    public UserResponse requestPasswordReset(@QueryParam("email") String email) {
        return userService.requestPasswordReset(email);
    }

    @POST
    @Path("/password/reset")
    @Timed
    public UserResponse resetPassword(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("token") String token) {
        return userService.resetPassword(email, password, token);
    }

    @POST
    @Path("/request/invite")
    @Timed
    public UserResponse requestInvite(@QueryParam("email") String email, @QueryParam("name") String name) {
        return userService.requestInvite(name, email);
    }

    @GET
    @Path("/get/role")
    @Timed
    public UserResponse getUserRole(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        return UserResponse.builder().success(true).role(role).id(authenticatedUser.getUserId()).build();
    }

    @GET
    @Path("/invites")
    @Timed
    public InvitesResponse getInvites(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        if(!role.equals("admin")) {
            return InvitesResponse.builder().success(false).build();
        }
        return userService.getInvites();
    }

    @GET
    @Path("/all")
    @Timed
    public UsersResponse getAllUsers(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        if(!role.equals("admin")) {
            return UsersResponse.builder().success(false).build();
        }
        return userService.getAllUsers();
    }

    @POST
    @Path("/edit")
    @Timed
    public UsersResponse editUser(User user, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        if(!role.equals("admin")) {
            return UsersResponse.builder().success(false).build();
        }
        return userService.updateUser(user);
    }

    @DELETE
    @Path("/invite/")
    @Timed
    public InvitesResponse deleteInvite(@QueryParam("email") String email, @CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        if(!role.equals("admin")) {
            return InvitesResponse.builder().success(false).build();
        }
        return userService.deleteInvite(email);
    }
}