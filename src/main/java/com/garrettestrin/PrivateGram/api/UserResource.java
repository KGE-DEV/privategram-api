package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.CommentResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.JWTToken;
import com.garrettestrin.PrivateGram.api.ApiObjects.Message;
import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.Auth.AuthenticatedUser;
//import com.garrettestrin.PrivateGram.biz.UserService;

// TODO: fix this import
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private Auth auth;

    public UserResource(Auth auth) {

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

    // TODO: JAVADOC
    // TODO: add error handling
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
}