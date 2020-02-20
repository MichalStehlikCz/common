package com.provys.common.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * Represents regular PROVYS exception - extends exception with supplied internal name
 */
public final class RegularException extends ProvysException {

  private final String nameNm;

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message, parameters and
   * cause. Note that the detail message associated with {@code cause} is not automatically
   * incorporated in this runtime exception's detail message.
   *
   * @param nameNm  is internal name of exception as registered in ERROR database object
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param params  is list of parameter and their values that can be embedded in error message
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). (A @{code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.)
   */
  public RegularException(String nameNm, String message, @Nullable Map<String, String> params,
      @Nullable Throwable cause) {
    super(nameNm + ": " + message, params, cause);
    this.nameNm = nameNm;
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message and parameters.
   *
   * @param nameNm  is internal name of exception as registered in ERROR database object
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param params  is list of parameter and their values that can be embedded in error message
   */
  public RegularException(String nameNm, String message, @Nullable Map<String, String> params) {
    this(nameNm, message, params, null);
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message and cause. Note
   * that the detail message associated with {@code cause} is not automatically incorporated in this
   * runtime exception's detail message.
   *
   * @param nameNm  is internal name of exception as registered in ERROR database object
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). (A {@code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.)
   */
  public RegularException(String nameNm, String message, @Nullable Throwable cause) {
    this(nameNm, message, Collections.emptyMap(), cause);
  }

  /**
   * Constructs a new PROVYS runtime exception with the specified detail message and cause.
   *
   * @param nameNm  is internal name of exception as registered in ERROR database object
   * @param message the detail message; displayed to user if translations via database are not
   *                available. Message is prefixed with internal name
   */
  public RegularException(String nameNm, String message) {
    this(nameNm, message, (Throwable) null);
  }

  @Override
  public String getNameNm() {
    return nameNm;
  }
}
