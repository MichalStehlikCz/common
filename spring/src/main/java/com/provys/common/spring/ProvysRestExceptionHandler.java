package com.provys.common.spring;

import com.provys.common.exception.ProvysException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception interceptor for Spring that logs exception and produces error responses in Provys
 * format.
 */
@ControllerAdvice
@Order(0)
class ProvysRestExceptionHandler {

  private static final Logger LOG = LogManager.getLogger(ProvysRestExceptionHandler.class);

  private static ResponseEntity<Object> buildResponse(WebRequest request, WsError error,
      int status) {
    var requestHeaders = request.getHeaderValues(HttpHeaders.ACCEPT);
    var response = ResponseEntity
        .status(status);
    if (requestHeaders != null) {
      List<MediaType> acceptHeader = MediaType.parseMediaTypes(Arrays.asList(requestHeaders));
      if (acceptHeader.stream().anyMatch(
          mediaType -> mediaType.isCompatibleWith(MediaType.APPLICATION_JSON) || mediaType
              .isCompatibleWith(MediaType.APPLICATION_XML))) {
        // JSON and XML are handled properly by container
        return response.body(error);
      }
      if (acceptHeader.stream()
          .anyMatch(mediaType -> mediaType.isCompatibleWith(MediaType.TEXT_PLAIN))) {
        return response
            .contentType(MediaType.TEXT_PLAIN)
            .body(error.getMessage());
      }
    }
    return response.body(null);
  }

  private static ResponseEntity<Object> buildResponse(WebRequest request, WsError error) {
    return buildResponse(request, error, ProvysException.HTTP_STATUS);
  }

  @ExceptionHandler(ProvysException.class)
  public ResponseEntity<Object> handleProvysException(ProvysException e, WebRequest request) {
    LOG.error("Unhandled Provys exception", e);
    return buildResponse(request, new WsError(e.getStatusCode(), e.getNameNm(), e.getMessage(),
        Arrays.toString(e.getStackTrace())), e.getHttpStatus());
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handleGenericException(Throwable e, WebRequest request) {
    LOG.error("Unhandled exception", e);
    return buildResponse(request,
        new WsError(ProvysException.STATUS_CODE, "INTERNAL_EXCEPTION", e.getMessage(),
            Arrays.toString(e.getStackTrace())));
  }
}
