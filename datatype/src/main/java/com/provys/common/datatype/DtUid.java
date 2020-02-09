package com.provys.common.datatype;

import com.provys.common.exception.InternalException;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Support for Provys domains UID and REF
 */
@SuppressWarnings("WeakerAccess")
@JsonbTypeAdapter(JsonbDtUidAdapter.class)
@XmlJavaTypeAdapter(XmlDtUidAdapter.class)
public class DtUid {

    /** Missing privileges indicator for Provys types UID and REF */
    public static final DtUid PRIV = new DtUid(BigInteger.valueOf(-2L));
    /** Multi-value indicator for Provys types UID and REF */
    public static final DtUid ME = new DtUid(BigInteger.valueOf(-1L));

    /**
     * Create DtUid value based on supplied BigInteger. Used when retrieving data from non-Provys source (for example
     * XML or JSON deserialization)
     *
     * @param value is value to be assigned to DtUid
     * @return DtUid value representing supplied number
     */
    public static DtUid valueOf(BigInteger value) {
        if (value.equals(PRIV.getValue())) {
            return PRIV;
        } else if (value.equals(ME.getValue())) {
            return ME;
        }
        return new DtUid(value);
    }

    /**
     * Create DtUid value based on supplied BigDecimal. Used when retrieving data from non-Provys source (for example
     * JDBC as it does not support reading BigInteger directly)
     *
     * @param value is value to be assigned to DtUid
     * @return DtUid value representing supplied number
     */
    public static DtUid valueOf(BigDecimal value) {
        try {
            return valueOf(value.toBigIntegerExact());
        } catch (ArithmeticException e) {
            throw new InternalException("Fractional part encountered when reading Uid");
        }
    }

    /**
     * Create DtUid value based on supplied long. Note that long cannot represent all allowed values and thus should
     * not be used for work with Provys Id values, but it is useful when writing tests as it is easier to write long
     * literal than BigInteger or BigDecimal
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
        return (value.compareTo(BigInteger.ZERO) > 0);
    }

    /**
     * Indicates if given value is multiline Uid. Multiline values are negative values with exception of ME and Priv
     *
     * @return true if Uid value is negative, not ME nor Priv
     */
    public boolean isMultiline() {
        return (value.compareTo(PRIV.value) < 0);
    }

    /**
     * @return if this value is PRIV
     */
    public boolean isPriv() {
        return equals(PRIV);
    }

    /**
     * @return if this value is ME (multivalue indicator)
     */
    public boolean isME() {
        return equals(ME);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtUid dtUid = (DtUid) o;
        return Objects.equals(value, dtUid.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (isME()) return "ID" + DtString.ME;
        if (isPriv()) return "ID" + DtString.PRIV;
        return "ID" + value;
    }
}
