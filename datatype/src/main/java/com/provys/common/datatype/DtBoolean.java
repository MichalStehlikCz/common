package com.provys.common.datatype;

import com.provys.common.exception.InternalException;

import javax.annotation.Nonnull;

/**
 * Utility class, containing methods relevant for working with Provys domain BOOLEAN. It is represented by boolean
 * values in Provys Java framework
 */
@SuppressWarnings("WeakerAccess")
public class DtBoolean {

    /**
     * Convert {@code String} retrieved from Provys database to boolean
     *
     * @param value is string value retrieved from Provys boolean column or function (char Y / N)
     * @return corresponding Java boolean value
     */
    public static boolean ofProvysDb(String value) {
        if (value.equals("Y")) {
            return true;
        } else if (value.equals("N")) {
            return false;
        }
        throw new InternalException("Invalid PROVYS boolean value '" + value + "'");
    }

    /**
     * Convert Java boolean value to {@code String}, representing this value in Provys database
     *
     * @param value is source boolean value
     * @return corresponding Provys database boolean value (char Y / N)
     */
    @Nonnull
    public static String toProvysDb(boolean value) {
        return value ? "Y" : "N";
    }

    /**
     * Non-instantiable utility class
     */
    private DtBoolean() {}
}
