package com.provys.common.jakarta;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

/**
 * Exception mapper for Jax-Rs that logs problem and translates generic exception to standard Provys
 * web-service error message format
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger LOG = LogManager.getLogger(GenericExceptionMapper.class);

  @Override
  public Response toResponse(Exception e) {
    LOG.error("Unhandled exception", e);
    return Response
        .status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(new WsError(-1, "INTERNAL_EXCEPTION", e.getMessage(),
            Arrays.toString(e.getStackTrace())))
        .build();
  }
}
