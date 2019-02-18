package com.provys.common.exception;

import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class RegularException extends ProvysException {

    @Nonnull
    private final String nameNm;
    @Nonnull
    private final Map<String, String> params;

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message, parameters and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this runtime exception's detail message.
     *
     * @param logger is logger for current class; exception is logger as error to logger
     * @param nameNm is internal name of exception as registered in ERROR database object
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params is list of parameter and their values that can be embedded in error message
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public RegularException(Logger logger, String nameNm, String message, Map<String, String> params, @Nullable Throwable cause) {
        super(nameNm + ": " + message, cause);
        this.nameNm = nameNm;
        this.params = new HashMap<>(params);
        logger.error("{}: {} {}", nameNm, message, params);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message and parameters.
     *
     * @param nameNm is internal name of exception as registered in ERROR database object
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param params is list of parameter and their values that can be embedded in error message
     */
    public RegularException(Logger logger, String nameNm, String message, Map<String, String> params) {
        this(logger, nameNm, message, params, null);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail
     * message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this runtime exception's detail message.
     *
     * @param nameNm is internal name of exception as registered in ERROR database object
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *             value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public RegularException(Logger logger, String nameNm, String message, @Nullable Throwable cause) {
        this(logger, nameNm, message, Collections.emptyMap(), cause);
    }

    /**
     * Constructs a new PROVYS runtime exception with the specified detail message and cause.
     *
     * @param nameNm is internal name of exception as registered in ERROR database object
     * @param message the detail message; displayed to user if translations via database are not available. Message is
     *               prefixed with internal name
     */
    public RegularException(Logger logger, String nameNm, String message) {
        this(logger, nameNm, message, (Throwable) null);
    }

    @Nonnull
    @Override
    public String getNameNm() {
        return nameNm;
    }

    /**
     * @return value of field params
     */
    @Nonnull
    @Override
    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

}
