package com.garrettestrin.PrivateGram.app.Auth;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import io.jsonwebtoken.Claims;

import javax.ws.rs.ext.ParamConverter;

public class AuthenticatedUserConverter implements ParamConverter<AuthenticatedUser> {
  private static Auth auth;

  public AuthenticatedUserConverter(PrivateGramConfiguration config) {
    this.auth = new Auth(config);
  }

  @Override
  public AuthenticatedUser fromString(String token) {
    if (token == null || token.trim().isEmpty()) {
      return null;
    }
        Claims claims = auth.verifyJWT(token);
        boolean isVerified = false;
        if(claims != null) {
            isVerified = !claims.getId().isEmpty();
        }

        return new AuthenticatedUser(claims.toString());

//    return new AuthenticatedUser(auth.createJWT("garrett.estrin@gmail.com", "Garrett", "elsie_gram_auth", -1));
  }

  @Override
  public String toString(AuthenticatedUser value) {
    return null;
  }

}

