package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.*;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
import com.garrettestrin.PrivateGram.biz.UserService;
//import com.garrettestrin.PrivateGram.biz.UserService;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserService userService;
    private Auth auth;

    private final String AUTH_COOKIE = "elsie_gram_auth";

    public UserResource(Auth auth, UserService userService) {
        this.userService = userService;
        this.auth = auth;
    }

    @POST
    @Path("/create/token")
    public JWTToken getToken(@QueryParam("secret") String secret, @QueryParam("user_id") String userId) {
        if(!secret.equals(auth.SECRET_KEY)) {
            return new JWTToken(null);
        }
        return new JWTToken(auth.createJWT(userId, "garrett.estrin.com", "elsie_gram_auth", -1));
    }

//    // TODO: JAVADOC
//    // TODO: add error handling
//    @POST
//    @Path("/register")
//    @Timed
//    public Message registerUser(@QueryParam("email") String email,
//                                @QueryParam("first_name") String first_name,
//                                @QueryParam("last_name") String last_name,
//                                @QueryParam("password") String password) {
//
//        return userService.registerUser(email, first_name, last_name, password);
//    }

    @POST
    @Path("/add")
    @Timed
    public Response addUser(@QueryParam("id") int id,
                            @QueryParam("email") String email,
                            @QueryParam("name") String name,
                            @QueryParam("password") String password,
                            @QueryParam("secret") String secret) throws UnsupportedEncodingException {
        if(secret.equals(auth.SECRET_KEY)) {
            userService.addUser(id, email, name, password);
            return Response.ok().build();
        }
        return Response.serverError().build();
    }

    // TODO: JAVADOC
    // TODO: add error handling
//    @POST
//    @Path("/login")
//    @Timed
//    public Message registerUser(@QueryParam("email") String email,
//                                @QueryParam("password") String password) {
//
//        return userService.loginUser(email, password);
//    }
//
//    // TODO: JAVADOC
//    // TODO: add error handling
//    @POST
//    @Path("/reset/password")
//    @Timed
//    public Message resetPassword(@QueryParam("email") String email) {
//
//        return userService.resetPassword(email);
//    }
//
//    @POST
//    @Path("/verify/token")
//    @Timed
//    public Message verifyToken(@QueryParam("token") String token,
//                               @QueryParam("Auth") String auth) {
//        return userService.verifyToken(token, auth);
//    }

    @GET
    @Path("/get/role")
    @Timed
    public UserResponse getUserRole(@CookieParam(AUTH_COOKIE) AuthenticatedUser authenticatedUser) {
        String role = userService.getUserRole(authenticatedUser.getUserId());
        return UserResponse.builder().success(true).role(role).id(authenticatedUser.getUserId()).build();
    }
}