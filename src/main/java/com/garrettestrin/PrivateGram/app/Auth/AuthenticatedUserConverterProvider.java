package com.garrettestrin.PrivateGram.app.Auth;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class AuthenticatedUserConverterProvider implements ParamConverterProvider {
  private final PrivateGramConfiguration config;

  public AuthenticatedUserConverterProvider(PrivateGramConfiguration config) {
    this.config = config;
  }
  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
                                            Annotation[] annotations) {
    if (rawType == AuthenticatedUser.class) {
      return (ParamConverter<T>) new AuthenticatedUserConverter(config);
    }
    return null;
  }
}
