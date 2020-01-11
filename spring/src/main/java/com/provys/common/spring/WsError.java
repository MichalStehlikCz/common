package com.provys.common.spring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.Optional;

/**
 * Class represents result of web-service call in standard PROVYS format (e.g. with data and error sections)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ERRORSTATUS")
public class WsError {

    @XmlElement(name = "STATUS")
    private final int status;
    @XmlElement(name = "ERROR_NM")
    @Nullable
    private final String errorNm;
    @XmlElement(name = "ERRORMESSAGE")
    @Nullable
    private final String message;
    @XmlElement(name = "ERRORSTACK")
    @Nullable
    private final String stack;

    /**
     * Private constructor used for JAXB deserialization; preferably Jackson is used and it is not needed, but...
     */
    private WsError() {
        status = 0;
        errorNm = null;
        message = null;
        stack = null;
    }

    @JsonCreator
    public WsError(@JsonProperty("STATUS") int status,
                   @JsonProperty("ERROR_NM") @Nullable String errorNm,
                   @JsonProperty("ERRORMESSAGE") @Nullable String message,
                   @JsonProperty("ERRORSTACK") @Nullable String stack) {
        this.status = status;
        this.errorNm = errorNm;
        this.message = message;
        this.stack = stack;
    }

    /**
     * @return value of field status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return value of field errorNm
     */
    @Nonnull
    public Optional<String> getErrorNm() {
        return Optional.ofNullable(errorNm);
    }

    /**
     * @return value of field message
     */
    @Nonnull
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    /**
     * @return value of field stack
     */
    @Nonnull
    public Optional<String> getStack() {
        return Optional.ofNullable(stack);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WsError wsError = (WsError) o;
        return status == wsError.status &&
                Objects.equals(errorNm, wsError.errorNm) &&
                Objects.equals(message, wsError.message) &&
                Objects.equals(stack, wsError.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorNm, message, stack);
    }

    @Override
    public String toString() {
        return "WsError{" +
                "status=" + status +
                ", errorNm='" + errorNm + '\'' +
                ", message='" + message + '\'' +
                ", stack='" + stack + '\'' +
                '}';
    }
}
