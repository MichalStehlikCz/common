package com.provys.common.spring;

import com.provys.common.exception.ProvysException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;

@ControllerAdvice
@Order(0)
class ProvysRestExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(ProvysRestExceptionHandler.class);

    @ExceptionHandler({ProvysException.class})
    public ResponseEntity<Object> handleProvysException(ProvysException e, WebRequest request) {
        LOG.error("Unhandled Provys exception", e);
        return new ResponseEntity<>(
                new WsError(-1, (e).getNameNm(), e.getMessage(), Arrays.toString(e.getStackTrace())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleGenericException(Throwable e, WebRequest request) {
        LOG.error("Unhandled exception", e);
        return new ResponseEntity<>(
                new WsError(-1, "INTERNAL_EXCEPTION", e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
