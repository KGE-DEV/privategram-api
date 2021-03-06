package com.garrettestrin.PrivateGram.biz;

import static org.apache.http.util.TextUtils.isEmpty;


import com.garrettestrin.PrivateGram.api.ApiObjects.*;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.DataObjects.Invite;
import com.garrettestrin.PrivateGram.data.DataObjects.ResetPasswordToken;
import com.garrettestrin.PrivateGram.data.DataObjects.User;
import com.garrettestrin.PrivateGram.data.UserDao;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserService {

    private final UserDao userDao;
    private final Auth auth;
    private final PrivateGramConfiguration config;
    private final BizUtilities bizUtilities;
    private final String wpUrl;
    private final String siteDomain;

    private final String AUTH_COOKIE = "api_auth";
    private int TEN_YEARS_IN_SECONDS = 10 * 365 * 24 * 60 * 60;

    public UserService(UserDao userDao, Auth auth, PrivateGramConfiguration config) {

        this.userDao = userDao;
        this.auth = auth;
        this.config = config;
        this.bizUtilities = new BizUtilities(config);
        this.wpUrl = config.getWpUrl();
        this.siteDomain = config.getSiteDomain();
    }

    @Deprecated
    public UserResponse loginUser(String email, String password, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User user = userDao.getUserByEmail(email);
        if(user == null) {
            return UserResponse.builder().success(false).build();
        }
        if(user.wp_pass) {
            URL url = new URL(wpUrl + "?action=microservice_login&email=" + email + "&password=" + password);
            InputStream is = url.openStream();
            try {
                String resultFromWP = CharStreams.toString(new InputStreamReader(
                        is, Charsets.UTF_8));
                JsonParser parser = new JsonParser();
                JsonElement jsonTree = parser.parse(resultFromWP);
                JsonObject jsonObject = jsonTree.getAsJsonObject();
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    setAuthCookie(request, response, user.id);
                }
                return UserResponse.builder().success(success).id(user.id).role(user.role).build();
            } finally {
                is.close();
            }
        } else {
            boolean isPasswordVerified = verifyPassword(email, password);
            if(isPasswordVerified) {
                setAuthCookie(request, response, user.id);
            }
            return UserResponse.builder().success(isPasswordVerified).id(user.id).role(user.role).build();
        }
    }

    public UserResponse loginUser(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User user = userDao.getUserByEmail(loginRequest.email);
        if(user == null) {
            return UserResponse.builder().success(false).build();
        }
        if(user.wp_pass) {
            URL url = new URL(wpUrl + "?action=microservice_login&email=" + loginRequest.email + "&password=" + loginRequest.password);
            InputStream is = url.openStream();
            try {
                String resultFromWP = CharStreams.toString(new InputStreamReader(
                        is, Charsets.UTF_8));
                JsonParser parser = new JsonParser();
                JsonElement jsonTree = parser.parse(resultFromWP);
                JsonObject jsonObject = jsonTree.getAsJsonObject();
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    setAuthCookie(request, response, user.id);
                }
                return UserResponse.builder().success(success).id(user.id).role(user.role).build();
            } finally {
                is.close();
            }
        } else {
            boolean isPasswordVerified = verifyPassword(loginRequest.email, loginRequest.password);
            if(isPasswordVerified) {
                setAuthCookie(request, response, user.id);
            }
            return UserResponse.builder().success(isPasswordVerified).id(user.id).role(user.role).build();
        }
    }

    public UserResponse addUser(String email, String name) {
        try {
            userDao.registerUser(email, name, auth.hashPassword(generateRandomPassword()));
            userDao.deactivateUserInvite(email);
            String token = generateResetToken();
            userDao.saveResetEmailToken(email, token, getTimeInXHours(48));
            bizUtilities.newUserPasswordSet(email, token);
            return UserResponse.builder().success(true).build();
        } catch (Exception e) {
            return UserResponse.builder().success(false).build();
        }
    }

    // TODO: JAVADOC
    // TODO: Add Test
    public boolean verifyPassword(String email, String password) {
        String hashedPassword = userDao.getHashedPasswordByEmail(email);
        String newHashedPassword = auth.hashPassword(password);
        if(!isEmpty(hashedPassword) && hashedPassword.equals(newHashedPassword))
            return true;
        return false;
    }


    // TODO: JAVADOC
    // TODO: Add Test
    public UserResponse requestPasswordReset(String email) {
        String token = generateResetToken();
        ResetPasswordToken tokenObject = userDao.checkForExistingResetToken(email);
        if(tokenObject == null) {
            userDao.saveResetEmailToken(email, token, getTimeInXHours(0));
        } else {
            userDao.deleteExistingResetPasswordToken(email);
            userDao.saveResetEmailToken(email, token, getTimeInXHours(48));
        }

        boolean emailSent = bizUtilities.sendResetPasswordEmail(email, token);
        return UserResponse.builder().success(emailSent).build();
    }

    public UserResponse resetPassword(String email, String password, String token, HttpServletResponse response, HttpServletRequest request) throws MalformedURLException {
        boolean wasPasswordReset = false;
        ResetPasswordToken resetPasswordToken = userDao.checkForExistingResetToken(email);
        if(resetPasswordToken != null && resetPasswordToken.token.equals(token)) {
            wasPasswordReset = userDao.resetPassword(email, auth.hashPassword(password));
            if(wasPasswordReset) {
                userDao.deleteExistingResetPasswordToken(email);
                setAuthCookie(request, response, userDao.getUserByEmail(email).id);
            }
        }
        return UserResponse.builder().success(wasPasswordReset).build();
    }

    public String getUserRole(int id) {
        String role = userDao.getUserRole(id);
        if(role.isEmpty()) {
            role = "subscriber";
        }
        return role;
    }

    public UserResponse requestInvite(String name, String email) {
        User user = userDao.getUserByEmail(email);
        if(user != null) {
            return UserResponse.builder().success(false).message("You are already a member. If you forgot your password, please use the forgot password link below.").build();
        }
        Invite invite = userDao.checkForExistingInvite(email);
        if(invite != null) {
            return UserResponse.builder().success(false).message("You have already requested an invite. Please wait to be accepted.").build();
        }
        try {
            userDao.saveInvite(email, name);
            bizUtilities.sendInviteRequestedEmail(userDao.getAdminUsers());
            return UserResponse.builder().success(true).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return UserResponse.builder().success(false).build();
    }

    public InvitesResponse getInvites() {
        return InvitesResponse.builder().success(true).invites(userDao.getInvites()).build();
    }

    private void setAuthCookie(HttpServletRequest request,  HttpServletResponse response, int userId) throws MalformedURLException {
        Cookie authCookie = new Cookie(AUTH_COOKIE, new JWTToken(auth.createJWT(userId, "garrett.estrin.com", AUTH_COOKIE, -1)).getToken());
        authCookie.setMaxAge(TEN_YEARS_IN_SECONDS);
        authCookie.setPath("/");
        String domain = new URL(request.getRequestURL().toString()).getHost();
        if(!domain.equals("localhost")) {
            domain = "." + siteDomain;
            authCookie.isHttpOnly();
            authCookie.setSecure(true);
        }
        authCookie.setDomain(domain);

        response.addCookie(authCookie);
    }

    private String generateRandomPassword() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    private String generateResetToken() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 60;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private Date getTimeInXHours(int hours) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
        return cal.getTime();
    }

  public UsersResponse getAllUsers() {
        return UsersResponse.builder().success(true).users(userDao.getAllUsers()).build();
  }

    public UsersResponse updateUser(com.garrettestrin.PrivateGram.api.ApiObjects.User user) {
        userDao.updateUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
        return getAllUsers();
    }

    public InvitesResponse deleteInvite(String email) {
        boolean wasInviteDeleted = userDao.deactivateUserInvite(email);
        return  InvitesResponse.builder().success(wasInviteDeleted).build();
    }

    public UserResponse deleteUser(int id) {
        boolean wasUserDeativated = userDao.deactivateUser(id);
        return UserResponse.builder().success(wasUserDeativated).build();
    }
}
