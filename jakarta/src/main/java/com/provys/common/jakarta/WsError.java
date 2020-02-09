package com.provys.common.jakarta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
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
@JsonbPropertyOrder({"status", "errorNm", "message", "stack"})
public class WsError {

    @XmlElement(name = "STATUS")
    private final int status;
    @XmlElement(name = "ERROR_NM")
    private final @Nullable String errorNm;
    @XmlElement(name = "ERRORMESSAGE")
    private final @Nullable String message;
    @XmlElement(name = "ERRORSTACK")
    private final @Nullable String stack;

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
    @JsonbCreator
    public WsError(@JsonProperty("STATUS") @JsonbProperty("STATUS") int status,
                   @JsonProperty("ERROR_NM") @JsonbProperty("ERROR_NM") @Nullable String errorNm,
                   @JsonProperty("ERRORMESSAGE") @JsonbProperty("ERRORMESSAGE") @Nullable String message,
                   @JsonProperty("ERRORSTACK") @JsonbProperty("ERRORSTACK") @Nullable String stack) {
        this.status = status;
        this.errorNm = errorNm;
        this.message = message;
        this.stack = stack;
    }

    /**
     * @return value of field status
     */
    @JsonbProperty("STATUS")
    public int getStatus() {
        return status;
    }

    /**
     * @return value of field errorNm
     */
    @JsonbProperty("ERROR_NM")
    public @Nullable String getErrorNm() {
        return errorNm;
    }

    /**
     * @return value of field message
     */
    @JsonbProperty("ERRORMESSAGE")
    public @Nullable String getMessage() {
        return message;
    }

    /**
     * @return value of field stack
     */
    @JsonbProperty("ERRORSTACK")
    public @Nullable String getStack() {
        return stack;
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
