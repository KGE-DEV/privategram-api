package com.garrettestrin.PrivateGram.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.PrivateGram.api.ApiObjects.Message;
import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.biz.UserService;

// TODO: fix this import
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserService userService;

    public UserResource(UserService userService) {

        this.userService = userService;
    }

    // TODO: JAVADOC
    @GET
    @Path("get/user/{id}")
    @Timed
    public User getUser(@PathParam("id") long id) {

        return userService.getUserById(id);
    }

    // TODO: JAVADOC
    // TODO: add error handling
    @POST
    @Path("/register")
    @Timed
    public Message registerUser(@QueryParam("email") String email,
                                @QueryParam("first_name") String first_name,
                                @QueryParam("last_name") String last_name,
                                @QueryParam("password") String password) {

        return userService.registerUser(email, first_name, last_name, password);
    }

    // TODO: JAVADOC
    // TODO: add error handling
    @POST
    @Path("/login")
    @Timed
    public Message registerUser(@QueryParam("email") String email,
                                @QueryParam("password") String password) {

        boolean isUserVefified = userService.verifyPassword(email, password);
        return new Message("User Verification", isUserVefified, 200);
    }
}