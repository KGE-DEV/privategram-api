package com.garrettestrin.PrivateGram.app.Auth;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.UserDao;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticatedUserConverterProvider implements ParamConverterProvider {
  private final PrivateGramConfiguration config;
  private final UserDao userDao;

  public AuthenticatedUserConverterProvider(PrivateGramConfiguration config, UserDao userDao) {

    this.config = config;
    this.userDao = userDao;
  }
  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
                                            Annotation[] annotations) {
    if (rawType == AuthenticatedUser.class) {
      return (ParamConverter<T>) new AuthenticatedUserConverter(config, userDao);
    }
    return null;
  }
}
