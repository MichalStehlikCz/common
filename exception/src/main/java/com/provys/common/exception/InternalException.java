package com.provys.common.exception;

import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * Represents internal exception. This exception can be used in internal checks, where other code should ensure
 * exception is not thrown. It should never be used for validation of external data
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class InternalException extends ProvysException {

    private static final String NAME_NM = "JAVA_INTERNAL_EXCEPTION";

    /**
     * Constructs a new PROVYS internal exception with the specified detail message, parameters and cause.
     * Note that the detail message associated with {@code cause} is not automatically incorporated in this runtime
     * exception's detail message.
     *
     * @param logger is logger for current class; exception is logger as error to logger
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params is list of parameter and their values that can be embedded in error message
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A @{code null}
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InternalException(Logger logger, String message, Map<String, String> params, @Nullable Throwable cause) {
        super(message, cause);
        logger.error("{}: {} {} {}", NAME_NM, message, params, cause);
    }

    /**
     * Constructs a new PROVYS internal exception with the specified detail message and parameters.
     *
     * @param logger is logger for current class; exception is logger as error to logger
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params is list of parameter and their values that can be embedded in error message
     */
    public InternalException(Logger logger, String message, Map<String, String> params) {
        this(logger, message, params, null);
    }

    /**
     * Constructs a new PROVYS internal exception with the specified detail message and cause.
     * Note that the detail message associated with {@code cause} is not
     * automatically incorporated in this runtime exception's detail message.
     *
     * @param logger is logger for current class; exception is logger as error to logger
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A @{code null}
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InternalException(Logger logger, String message, @Nullable Throwable cause) {
        this(logger, message, Collections.emptyMap(), cause);
    }

    /**
     * Constructs a new PROVYS internal exception with the specified detail message.
     *
     * @param logger is logger for current class; exception is logger as error to logger
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     */
    public InternalException(Logger logger, String message) {
        this(logger, message, (Throwable) null);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return NAME_NM;
    }
}
