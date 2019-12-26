package com.provys.common.ws;

import com.provys.common.exception.ProvysException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class ProvysExceptionMapper implements ExceptionMapper<ProvysException> {
    @Override
    public Response toResponse(ProvysException e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new WsError(-1, (e).getNameNm(), e.getMessage(), Arrays.toString(e.getStackTrace())))
                .build();
    }
}
