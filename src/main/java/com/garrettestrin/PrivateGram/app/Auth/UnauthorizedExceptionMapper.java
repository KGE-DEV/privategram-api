package com.garrettestrin.PrivateGram.app.Auth;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
  public Response toResponse(UnauthorizedException exception) {
    return Response.status(exception.getCode())
            .entity(exception.getMessage())
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build();
  }
}
