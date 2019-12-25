package com.provys.common.ws;

import com.provys.common.exception.ProvysException;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.function.Supplier;

public class ProvysResponse {
    public static <T> Response execute(Supplier<T> supplier) {
        try {
            return Response
                    .ok()
                    .entity(supplier.get())
                    .build();
        } catch (ProvysException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new WsError(-1, (e).getNameNm(), e.getMessage(), Arrays.toString(e.getStackTrace())))
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new WsError(-1, "INTERNAL_EXCEPTION", e.getMessage(),
                            Arrays.toString(e.getStackTrace())))
                    .build();
        }
    }
}
