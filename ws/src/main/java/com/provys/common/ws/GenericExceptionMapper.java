package com.provys.common.ws;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new WsError(-1, "INTERNAL_EXCEPTION", e.getMessage(),
                        Arrays.toString(e.getStackTrace())))
                .build();
    }
}
