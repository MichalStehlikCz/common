package com.provys.common.datatype;

/**
 * Implements support for Provys datatype VARCHAR (domains NAME, VARCHAR2 etc.)
 */
@SuppressWarnings("WeakerAccess")
public class DtString {

    /** Missing privileges indicator for Provys types VARCHAR2 and similar */
    public static final String PRIV = "##########";
    /** Multi-value indicator for Provys type VARCHAR2 */
    public static final String ME = "**********";

    /** Indicates if supplied value is regular (e.g. not one of special values).
     *
     * @param value is the value to be checked
     * @return true if supplied value is regular value, false otherwise
     */
    public static boolean isRegular(String value) {
        return (!value.equals(PRIV) && !value.equals(ME));
    }

    /**
     * DtString is utility class used for manipulation with String values in Provys framework and this is why its
     * constructor is not accessible
     */
    private DtString() {}
}