package com.provys.common.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * Common ancestor for exceptions thrown by PROVYS code.
 * Makes it easier to track all defined exceptions, adds mapping to PROVYS registered errors and logs exception.
 *
 * @author stehlik
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ProvysException extends RuntimeException {

    /**
     * Constructs a new PROVYS runtime exception with the specified detail
     * message and cause.
     * Note that the detail message associated with {@code cause} is not
     * automatically incorporated in this runtime exception's detail message.
     *
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A @{code null}
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ProvysException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message.
     *
     * @param message the detail message; displayed to user if translations via database are not available
     */
    public ProvysException(String message) {
        super(message);
    }

    /**
     * @return internal name of exception for mapping with provys ERROR object
     */
    @Nonnull
    public abstract String getNameNm();

    /**
     * Retrieve map of parameters containing additional information related to exception.
     *
     * @return empty map in this default implementation
     */
    @Nonnull
    public Map<String, String> getParams() {
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return getClass().getName() + "{nameNm=\"" + getNameNm() + "\", message=\"" + getMessage() +
                "\", params=" + getParams() + ", cause=" + getCause() + "}";
    }
}
