package com.provys.common.datatype;

/**
 * Implements support for Provys domain INTEGER
 */
public class DtInteger {

    /** Missing privileges indicator for Provys type INTEGER */
    public static final Integer PRIV = -2135412459;
    /** Multi-value indicator for Provys type INTEGER */
    public static final Integer ME = -2135412458;
    /** Minimal valid Provys INTEGER value */
    public static final Integer MIN = -2135412457;
    /** Maximal valid Provys INTEGER value */
    public static final Integer MAX = Integer.MAX_VALUE;

    /**
     * Indicates if supplied value is valid (e.g. regular or special value)
     *
     * @param value is the value to be checked
     * @return true if supplied value is regular value from within MIN..MAX interval or special value, false otherwise
     */
    public static boolean isValid(int value) {
        return ((value >= MIN) && (value <= MAX)) || (value == PRIV) || (value == ME);
    }

    /**
     * Indicates if supplied value is regular (e.g. not one of special values, inside interval of MIN .. MAX).
     *
     * @param value is the value to be checked
     * @return true if supplied value is regular value from within MIN..MAX interval, false otherwise
     */
    public static boolean isRegular(int value) {
        return (value > MIN) && (value < MAX);
    }

    /**
     * Indicates if supplied value can be valid value held in INTEGER property. Note that most integer properties can
     * only hold regular values, nevertheless there might be some situations (especially when property indicates lower
     * or upper bound) when even MIN and MAX values are valid.
     *
     * @param value is the value to be checked
     * @return true if supplied value is from (inclusive) MIN .. MAX interval, false otherwise
     */
    public static boolean isValidValue(int value) {
        return (value >= MIN) && (value <= MAX);
    }

    /**
     * DtInteger is utility class used for manipulation with Integer values in Provys framework and this is why its
     * constructor is not accessible
     */
    private DtInteger() {};
}
