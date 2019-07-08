package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Message;
import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.data.UserDao;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
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
    public Message registerUser(String email, String first_name, String last_name, String password) {

        try {
            Long userId = getUserIdByEmail(email);
            if(userId != null) return new Message("User already exists", false, 200);
            boolean userCreated = userDao.registerUser(email, first_name, last_name, password);
            return new Message("User was successfully created", userCreated, 200);
        } catch (Exception e) {
            return new Message("Something went wrong.", false, 500);
        }
    }
}
