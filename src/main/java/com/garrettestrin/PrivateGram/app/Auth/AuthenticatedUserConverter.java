package com.garrettestrin.PrivateGram.app.Auth;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.UserIdentity;

import javax.ws.rs.ext.ParamConverter;

public class AuthenticatedUserConverter implements ParamConverter<AuthenticatedUser> {
  private static Auth auth;

  public AuthenticatedUserConverter(PrivateGramConfiguration config) {
    this.auth = new Auth(config);
  }

  @Override
  public AuthenticatedUser fromString(String token) throws UnauthorizedException {
    if (token == null || token.trim().isEmpty()) {
      throw new UnauthorizedException();
    }

    Claims claims = auth.verifyJWT(token);
    if(claims.getId().isEmpty()) {
      throw new UnauthorizedException();
    }
    return new AuthenticatedUser(claims.getId(), token);
  }

  @Override
  public String toString(AuthenticatedUser value) {
    return null;
  }

}

