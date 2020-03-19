package com.garrettestrin.PrivateGram.app.Auth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedException extends WebApplicationException {

  static final String UNAUTHORIZED_USER_MESSAGE = "{\"message\":\"User is unauthorized\", \"success\":false}";
  private int code;
  public UnauthorizedException() {
    this(401);
  }
  public UnauthorizedException(int code) {
    this(code, UNAUTHORIZED_USER_MESSAGE, null);
  }
  public UnauthorizedException(int code, String message) {
    this(code, message, null);
  }
  public UnauthorizedException(int code, String message, Throwable throwable) {
    super(message, throwable);
    this.code = code;
  }
  public int getCode() {
    return code;
  }
}
