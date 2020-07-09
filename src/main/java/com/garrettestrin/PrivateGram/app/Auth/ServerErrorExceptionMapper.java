package com.garrettestrin.PrivateGram.app.Auth;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.biz.BizUtilities;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper implements ExceptionMapper<Exception> {
  private final PrivateGramConfiguration config;
  private final int MAX_THREADS = 256;
  private final Executor executor = Executors.newFixedThreadPool(MAX_THREADS);

  public ServerErrorExceptionMapper(PrivateGramConfiguration config) {
    this.config = config;
  }


  public Response toResponse(Exception exception) {
    BizUtilities bizUtilities = new BizUtilities(config);
    executor.execute(() -> {
      bizUtilities.sendServerErrorEmail(stackToString(exception));
    });
    return Response.status(500)
            .entity("{\"message\":\"An internal error occurred\"}")
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
