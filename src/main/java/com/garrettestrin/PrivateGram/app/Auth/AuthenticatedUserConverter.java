package com.garrettestrin.PrivateGram.app.Auth;

import static java.lang.Integer.parseInt;


import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.UserDao;
import io.jsonwebtoken.Claims;
import javax.ws.rs.ext.ParamConverter;

public class AuthenticatedUserConverter implements ParamConverter<AuthenticatedUser> {
  private static Auth auth;
  private static UserDao userDao;

  public AuthenticatedUserConverter(PrivateGramConfiguration config, UserDao userDao) {

    this.auth = new Auth(config);
    this.userDao = userDao;
  }

  @Override
  public AuthenticatedUser fromString(String token) throws UnauthorizedException {
    if (token == null || token.trim().isEmpty()) {
      throw new UnauthorizedException();
    }

    Claims claims = auth.verifyJWT(token);
    String userId = claims.getId();
    if(userId.isEmpty()) {
      throw new UnauthorizedException();
    }
    String role = userDao.getUserRole(parseInt(userId));
    boolean isAdmin = role.equalsIgnoreCase("admin") ? true : false;
    return AuthenticatedUser.builder().userId(parseInt(userId)).token(token).isAdmin(isAdmin).build();
  }

  @Override
  public String toString(AuthenticatedUser value) {
    return null;
  }

}

