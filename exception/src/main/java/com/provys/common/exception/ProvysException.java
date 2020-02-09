package com.provys.common.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Common ancestor for exceptions thrown by PROVYS code.
 * Makes it easier to track all defined exceptions, adds mapping to PROVYS registered errors.
 *
 * @author stehlik
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ProvysException extends RuntimeException {

    private final Map<String, String> params;

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message, parameters and cause.
     * Note that the detail message associated with {@code cause} is not automatically incorporated in this runtime
     * exception's detail message.
     *
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params are additional parameters for exception. Parameters are saved and can be retrieved, e.g. when
     *              creating (translated) message for given exception
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A @{code null}
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ProvysException(String message, @Nullable Map<String, String> params, @Nullable Throwable cause) {
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
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params are additional parameters for exception. Parameters are saved and can be retrieved, e.g. when
     *              creating (translated) message for given exception
     */
    public ProvysException(String message, @Nullable Map<String, String> params) {
        this(message, params, null);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message and cause.
     *
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A @{code null}
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ProvysException(String message, Throwable cause) {
        this(message, null, cause);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message.
     *
     * @param message the detail message; displayed to user if translations via database are not available
     */
    public ProvysException(String message) {
        this(message, null, null);
    }

     /**
     * @return internal name of exception for mapping with provys ERROR object
     */
    public abstract String getNameNm();

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
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return Objects.requireNonNull(super.getMessage()); // We do not allow creation without message
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvysException that = (ProvysException) o;
        return params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{nameNm=\"" + getNameNm() + "\", message=\"" + getMessage() +
                "\", params=" + getParams() + ", cause=" + getCause() + "}";
    }
}
