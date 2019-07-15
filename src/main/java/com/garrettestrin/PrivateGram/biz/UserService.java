package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Message;
import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.UserDao;
import io.jsonwebtoken.Claims;

public class UserService {

    private final UserDao userDao;
    private final Auth auth;
    private final PrivateGramConfiguration config;
    private final String AUTH_TOKEN;

    public UserService(UserDao userDao, Auth auth, PrivateGramConfiguration config) {

        this.userDao = userDao;
        this.auth = auth;
        this.config = config;
        this.AUTH_TOKEN = config.getAuthToken();
    }

    // TODO: JAVADOC
    // TODO: Rename Test
    public User getUserById(long id){

        return userDao.getUserById(id);
    }

    // TODO: JAVADOC
    // TODO: Add test
    public Long getUserIdByEmail(String email) {

        return userDao.getUserIdByEmail(email);
    }

    // TODO: JAVADOC
    // TODO: Add test
    public Message loginUser(String email, String password) {
        boolean isUserVefified = verifyPassword(email, password);
        if(isUserVefified)
            return new Message("User Verification", isUserVefified, 200, auth.createJWT(email, "Garrett", "user validated", -1));
        return new Message("User Verification", isUserVefified, 200, null);
    }

    // TODO: JAVADOC
    // TODO: Add test
    public Message registerUser(String email, String first_name, String last_name, String password) {

        try {
            Long userId = getUserIdByEmail(email);
            if(userId != null) return new Message("User already exists", false, 200, null);
            // hash password string
            String hashedPassword = auth.hashPassword(password);
            boolean userCreated = userDao.registerUser(email, first_name, last_name, hashedPassword);
            return loginUser(email, password);
        } catch (Exception e) {
            return new Message("Something went wrong.", false, 500, null);
        }
    }

    // TODO: JAVADOC
    // TODO: Add Test
    public boolean verifyPassword(String email, String password) {
        String hashedPassword = userDao.getHashedPasswordByEmail(email);
        String newHashedPassword = auth.hashPassword(password);
        if(hashedPassword.equals(newHashedPassword))
            return true;
        return false;
    }

    // TODO: JAVADOC
    // TODO: Add Test
    public Message verifyToken(String token, String auth) {
        if(!auth.equals(AUTH_TOKEN)) {
            return unauthorized();
        }
        Claims claims = Auth.verifyJWT(token);
        boolean isVerified = false;
        if(claims != null) {
            isVerified = !claims.getId().isEmpty();
        }
        return new Message("Token Verification", isVerified, 200, null);
    }

    public Message unauthorized() {
        return new Message("Unauthorized", false, 401, null);
    }
}
