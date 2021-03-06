package com.garrettestrin.PrivateGram.app;

import com.garrettestrin.PrivateGram.biz.BizUtilities;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper implements ExceptionMapper<Exception> {
  private final PrivateGramConfiguration config;
  private final int MAX_THREADS = 50;
  private final Executor executor = Executors.newFixedThreadPool(MAX_THREADS);

  public ServerErrorExceptionMapper(PrivateGramConfiguration config) {
    this.config = config;
  }


  public Response toResponse(Exception exception) {
    BizUtilities bizUtilities = new BizUtilities(config);
    executor.execute(() -> {
      // Ignore 404 errors
      // Google's crawler is annoying
      if (!exception.getMessage().equals("HTTP 404 Not Found")) {
        bizUtilities.sendServerErrorEmail(stackToString(exception));
      }
    });
    return Response.status(((NotFoundException) exception).getResponse().getStatus())
            .entity(String.format("{\"message\":\"%s\"}", exception.getMessage()))
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build();
  }

  private String stackToString(Exception e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    return sw.toString();
  }
}
