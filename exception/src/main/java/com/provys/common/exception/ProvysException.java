package com.provys.common.exception;

import static org.checkerframework.checker.nullness.NullnessUtil.castNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Common ancestor for exceptions thrown by PROVYS code. Makes it easier to track all defined
 * exceptions, adds mapping to PROVYS registered errors.
 *
 * @author stehlik
 */
@SuppressWarnings("AbstractClassExtendsConcreteClass")
public abstract class ProvysException extends RuntimeException {

  /**
   * Default Provys status code (used in web-service response).
   */
  public static final int STATUS_CODE = -1;
  /**
   * Default returned HTTP status (server error).
   */
  public static final int HTTP_STATUS = 500;
  private static final long serialVersionUID = 7155232755615842135L;

  private final Map<String, String> params;

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message, parameters and
   * cause. Note that the detail message associated with {@code cause} is not automatically
   * incorporated in this runtime exception's detail message.
   *
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param params  are additional parameters for exception. Parameters are saved and can be
   *                retrieved, e.g. when creating (translated) message for given exception
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). (A @{code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.)
   */
  public ProvysException(String message, @Nullable Map<String, String> params,
      @Nullable Throwable cause) {
    super(message, cause);
    if (params == null) {
      this.params = Collections.emptyMap();
    } else {
      this.params = Map.copyOf(params);
    }
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message and parameters.
   *
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param params  are additional parameters for exception. Parameters are saved and can be
   *                retrieved, e.g. when creating (translated) message for given exception
   */
  public ProvysException(String message, @Nullable Map<String, String> params) {
    this(message, params, null);
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message and cause.
   *
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). (A @{code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.)
   */
  public ProvysException(String message, Throwable cause) {
    this(message, null, cause);
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message.
   *
   * @param message the detail message; displayed to user if translations via database are not
   *                available
   */
  public ProvysException(String message) {
    this(message, null, null);
  }

  /**
   * Get internal name of Exception. Used for mapping to registered ERROR
   *
   * @return internal name of exception
   */
  public abstract String getNameNm();

  /**
   * Provys status code, associated with the exception. Fixed as default error in this ancestor, can
   * be modified if some exception returns specific error codes.
   *
   * @return Provys status code
   */
  @SuppressWarnings("SameReturnValue")
  public int getStatusCode() {
    return STATUS_CODE;
  }

  /**
   * Http status code, associated with the exception. Fixed as server error in this ancestor, can be
   * modified if some exception should return different http status code when caught in web service.
   *
   * @return http status code, 500 for this class
   */
  @SuppressWarnings("SameReturnValue")
  public int getHttpStatus() {
    return HTTP_STATUS;
  }

  /**
   * Retrieve map of parameters containing additional information related to exception.
   *
   * @return empty map in this default implementation
   */
  public Map<String, String> getParams() {
    return params;
  }

  /**
   * Returns the detail message string of this throwable.
   *
   * @return the detail message string of this {@code Throwable} instance (which may be {@code
   * null}).
   */
  @Override
  public String getMessage() {
    return castNonNull(super.getMessage()); // We do not allow creation without message
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProvysException)) {
      return false;
    }
    ProvysException that = (ProvysException) o;
    return Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return params != null ? params.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "ProvysException{"
        + "params=" + params
        + ", " + super.toString() + '}';
  }
}
