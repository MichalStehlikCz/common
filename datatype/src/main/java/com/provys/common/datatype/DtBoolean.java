package com.provys.common.datatype;

import com.provys.common.exception.InternalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class, containing methods relevant for working with Provys domain BOOLEAN. It is represented by boolean
 * values in Provys Java framework
 */
@SuppressWarnings("WeakerAccess")
public class DtBoolean {

    private static final Logger LOG = LogManager.getLogger(DtBoolean.class);

    /**
     * Parse provys boolean value (char Y/N) to Java boolean
     */
    public static boolean parseProvysValue(char value) {
        if (value == 'Y') {
            return true;
        } else if (value == 'N') {
            return false;
        }
        throw new InternalException(LOG, "Invalid PROVYS boolean value '" + value + "'");
    }

    /**
     * Convert boolean to Provys boolean value (Y/N)
     */
    public static char toProvysValue(boolean value) {
        return value ? 'Y' : 'N';
    }

    /**
     * Non-instantiable utility class
     */
    private DtBoolean() {}
}
