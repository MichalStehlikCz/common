package com.provys.common.spring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Class represents result of web-service call in standard PROVYS format (e.g. with data and error
 * sections)
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ERRORSTATUS")
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
   * Private constructor used for JAXB deserialization; preferably Jackson is used and it is not
   * needed, but...
   */
  private WsError() {
    status = 0;
    errorNm = null;
    message = null;
    stack = null;
  }

  /**
   * Default constructor for WsError. Supplying all properties.
   *
   * @param status is reported error status
   * @param errorNm is internal name of error
   * @param message is error message
   * @param stack is error stack
   */
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
   * Value of property status.
   *
   * @return value of property status
   */
  public int getStatus() {
    return status;
  }

  /**
   * Value of property errorNm.
   *
   * @return value of property errorNm
   */
  public @Nullable String getErrorNm() {
    return errorNm;
  }

  /**
   * Value of property message.
   *
   * @return value of property message
   */
  public @Nullable String getMessage() {
    return message;
  }

  /**
   * Value of property stack.
   *
   * @return value of property stack
   */
  public @Nullable String getStack() {
    return stack;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WsError)) {
      return false;
    }
    WsError wsError = (WsError) o;
    return status == wsError.status
        && Objects.equals(errorNm, wsError.errorNm)
        && Objects.equals(message, wsError.message)
        && Objects.equals(stack, wsError.stack);
  }

  @Override
  public int hashCode() {
    int result = status;
    result = 31 * result + (errorNm != null ? errorNm.hashCode() : 0);
    result = 31 * result + (message != null ? message.hashCode() : 0);
    result = 31 * result + (stack != null ? stack.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "WsError{"
        + "status=" + status
        + ", errorNm='" + errorNm + '\''
        + ", message='" + message + '\''
        + ", stack='" + stack + '\''
        + '}';
  }
}
