package com.provys.common.datatype;

import com.google.errorprone.annotations.Immutable;
import com.provys.common.exception.InternalException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Support for Provys domains UID and REF.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency on serialization proxy
@Immutable
public final class DtUid implements Serializable {

  /**
   * Missing privileges indicator for Provys types UID and REF.
   */
  public static final DtUid PRIV = new DtUid(BigInteger.valueOf(-2L));
  /**
   * Multi-value indicator for Provys types UID and REF.
   */
  public static final DtUid ME = new DtUid(BigInteger.valueOf(-1L));

  /**
   * Create DtUid value based on supplied BigInteger. Used when retrieving data from non-Provys
   * source (for example XML or JSON deserialization)
   *
   * @param value is value to be assigned to DtUid
   * @return DtUid value representing supplied number
   */
  public static DtUid valueOf(BigInteger value) {
    if (value.equals(PRIV.getValue())) {
      return PRIV;
    }
    if (value.equals(ME.getValue())) {
      return ME;
    }
    return new DtUid(value);
  }

  /**
   * Create DtUid value based on supplied BigDecimal. Used when retrieving data from non-Provys
   * source (for example JDBC as it does not support reading BigInteger directly)
   *
   * @param value is value to be assigned to DtUid
   * @return DtUid value representing supplied number
   */
  public static DtUid valueOf(BigDecimal value) {
    try {
      return valueOf(value.toBigIntegerExact());
    } catch (ArithmeticException e) {
      throw new InternalException("Fractional part encountered when reading Uid", e);
    }
  }

  /**
   * Create DtUid value based on supplied long. Note that long cannot represent all allowed values
   * and thus should not be used for work with Provys Id values, but it is useful when writing tests
   * as it is easier to write long literal than BigInteger or BigDecimal
   *
   * @param value is value to be assigned to DtUid
   * @return DtUid value representing supplied number
   */
  public static DtUid valueOf(String value) {
    return valueOf(new BigInteger(value));
  }

  private final BigInteger value;

  private DtUid(BigInteger value) {
    this.value = Objects.requireNonNull(value);
  }

  /**
   * BigInteger value of this UID.
   *
   * @return BigInteger value of this UID
   */
  public BigInteger getValue() {
    return value;
  }

  /**
   * Indicates if given value is regular Uid. Regular values are positive values
   *
   * @return true if Uid value is positive, false otherwise
   */
  public boolean isRegular() {
    return value.compareTo(BigInteger.ZERO) > 0;
  }

  /**
   * Indicates if given value is multiline Uid. Multiline values are negative values with exception
   * of ME and Priv
   *
   * @return true if Uid value is negative, not ME nor Priv
   */
  public boolean isMultiline() {
    return value.compareTo(PRIV.getValue()) < 0;
  }

  /**
   * Indicates this value is PRIV.
   *
   * @return if this value is PRIV
   */
  public boolean isPriv() {
    return equals(PRIV);
  }

  /**
   * Indicates this value is ME.
   *
   * @return if this value is ME (multivalue indicator)
   */
  public boolean isME() {
    return equals(ME);
  }

  /**
   * Supports serialization via SerializationProxy.
   *
   * @return proxy, corresponding to this DtUid
   */
  private Object writeReplace() {
    return new SerializationProxy(this);
  }

  /**
   * Should be serialized via proxy, thus no direct deserialization should occur.
   *
   * @param stream is stream from which object is to be read
   * @throws InvalidObjectException always
   */
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Use Serialization Proxy instead.");
  }

  private static final class SerializationProxy implements Serializable {

    private static final long serialVersionUID = -9017913400633080387L;
    private @Nullable BigInteger value;

    SerializationProxy() {
    }

    SerializationProxy(DtUid value) {
      this.value = value.value;
    }

    private Object readResolve() throws InvalidObjectException {
      if (value == null) {
        throw new InvalidObjectException("Value not read during DtUid deserialization");
      }
      return valueOf(value);
    }
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DtUid)) {
      return false;
    }
    DtUid dtUid = (DtUid) o;
    return value.equals(dtUid.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    if (isME()) {
      return DtString.ME;
    }
    if (isPriv()) {
      return DtString.PRIV;
    }
    return "ID" + value;
  }
}
