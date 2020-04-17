package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Invite;
import com.garrettestrin.PrivateGram.api.ApiObjects.JWTToken;
import com.garrettestrin.PrivateGram.api.ApiObjects.UserResponse;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
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

import static org.apache.http.util.TextUtils.isEmpty;

public class UserService {

    private final UserDao userDao;
    private final Auth auth;
    private final PrivateGramConfiguration config;
    private final String AUTH_TOKEN;
    private final BizUtilities bizUtilities;
    private final String wpUrl;

    private final String AUTH_COOKIE = "elsie_gram_auth";
    private int TEN_YEARS_IN_SECONDS = 10 * 365 * 24 * 60 * 60;

    public UserService(UserDao userDao, Auth auth, PrivateGramConfiguration config) {

        this.userDao = userDao;
        this.auth = auth;
        this.config = config;
        this.AUTH_TOKEN = config.getAuthToken();
        this.bizUtilities = new BizUtilities(config.getEmailUser(), config.getEmailHost(), config.getEmailPassword());
        this.wpUrl = config.getWpUrl();
    }

    // TODO: JAVADOC
    // TODO: Add test
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
            return UserResponse.builder().success(isPasswordVerified).id(user.id).role(user.role).build();
        }
    }

    public UserResponse addUser(String email, String name) {
        try {
            userDao.registerUser(email, name, auth.hashPassword(generateRandomPassword()));
            String token = generateResetToken();
            userDao.saveResetEmailToken(email, token, getTimeInXHours(48));
            bizUtilities.newUserPasswordReset(email, token);
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

    public UserResponse resetPassword(String email, String password, String token) {
        boolean wasPasswordReset = false;
        ResetPasswordToken resetPasswordToken = userDao.checkForExistingResetToken(email);
        if(resetPasswordToken.token.equals(token)) {
            wasPasswordReset = userDao.resetPassword(email, auth.hashPassword(password));
            if(wasPasswordReset) {
                userDao.deleteExistingResetPasswordToken(email);
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

    public UserResponse requestInvite(Invite invite) {
        try {
            userDao.saveInvite(invite.email, invite.name);
            bizUtilities.sendInviteRequestedEmail();
            return UserResponse.builder().success(true).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return UserResponse.builder().success(false).build();
    }

    private void setAuthCookie(HttpServletRequest request,  HttpServletResponse response, int userId) throws MalformedURLException {
        Cookie authCookie = new Cookie(AUTH_COOKIE, new JWTToken(auth.createJWT(userId, "garrett.estrin.com", "elsie_gram_auth", -1)).getToken());
        authCookie.setMaxAge(TEN_YEARS_IN_SECONDS);
        authCookie.setPath("/");
        String domain = new URL(request.getRequestURL().toString()).getHost();
        if(!domain.equals("localhost")) {
            domain = ".elsiegram.com";
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
}
