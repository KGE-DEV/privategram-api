package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.api.ApiObjects.Message;
import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.DataObjects.ResetPasswordToken;
import com.garrettestrin.PrivateGram.data.UserDao;
import com.garrettestrin.PrivateGram.biz.BizObjects.ValidatedUserInformation;

import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class UserService {

    private final UserDao userDao;
    private final Auth auth;
    private final PrivateGramConfiguration config;
    private final String AUTH_TOKEN;
    private final BizUtilities bizUtilities;

    public UserService(UserDao userDao, Auth auth, PrivateGramConfiguration config) {

        this.userDao = userDao;
        this.auth = auth;
        this.config = config;
        this.AUTH_TOKEN = config.getAuthToken();
        this.bizUtilities = new BizUtilities(config.getEmailUser(), config.getEmailHost(), config.getEmailPassword());
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
            return new Message("User Login", isUserVefified, 200, auth.createJWT(email, "Garrett", "user validated", -1));
        return new Message("User Login", isUserVefified, 200, null);
    }

    // TODO: JAVADOC
    // TODO: Add test
    public Message registerUser(String email, String first_name, String last_name, String password) {

        try {
            Long userId = getUserIdByEmail(email);
            if(userId != null) return new Message("User already exists", false, 200, null);
            ValidatedUserInformation validUserInformation = validateUserInformation(email, first_name, last_name, password);
            if(!validUserInformation.email || !validUserInformation.first_name || !validUserInformation.last_name || !validUserInformation.password)
                return new Message("Invalid information", false, 200, validUserInformation.toJSON());
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
        try {
            if(!auth.equals(AUTH_TOKEN)) {
                return unauthorized();
            }
        } catch (Exception e) {
            return unauthorized();
        }
        Claims claims = Auth.verifyJWT(token);
        boolean isVerified = false;
        if(claims != null) {
            isVerified = !claims.getId().isEmpty();
        }
        return new Message("Token Verification", isVerified, 200, null);
    }

    // TODO: JAVADOC
    // TODO: Add Test
    public Message resetPassword(String email) {
        Message message = new Message("Password reset email sent to " + email, true, 200, null);
        String token = generateResetToken();
        ResetPasswordToken tokenObject = userDao.checkForExistingResetToken(email);

        if(tokenObject == null) {
            userDao.resetPassword(email, token, getTimeInXHours(0));
        } else if(Calendar.getInstance().getTime().before(tokenObject.expiration)) {
            token = tokenObject.token;
        } else {
            userDao.deleteExistingResetPasswordToken(email);
            userDao.resetPassword(email, token, getTimeInXHours(48));
        }

        boolean emailSent = bizUtilities.sendEmail(email, "Password Reset Link", "Here is your password reset token: " + token);
        if(!emailSent)
            message.setMessage("Password Email was not sent");
        return message;
    }

    public ValidatedUserInformation validateUserInformation(String email, String first_name, String last_name, String password) {
        return new ValidatedUserInformation(validateEmail(email), validateName(first_name), validateName(last_name), validatePassword(password));
    }

    public boolean validateName(String name) {
        return name.matches( "[a-zA-Z]+([ '-][a-zA-Z]+)*" );
    }

    public boolean validateEmail(String email) {
        return email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$");
    }

    private Date getTimeInXHours(int hours) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
        return cal.getTime();
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

    public Message unauthorized() {
        return new Message("Unauthorized", false, 401, null);
    }
}
