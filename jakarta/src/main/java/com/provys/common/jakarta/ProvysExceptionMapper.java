package com.provys.common.jakarta;

import com.provys.common.exception.ProvysException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class ProvysExceptionMapper implements ExceptionMapper<ProvysException> {

    private static final Logger LOG = LogManager.getLogger(ProvysExceptionMapper.class);

    @Override
    public Response toResponse(ProvysException e) {
        LOG.error("Unhandled exception", e);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new WsError(-1, (e).getNameNm(), e.getMessage(), Arrays.toString(e.getStackTrace())))
                .build();
    }
}
