package com.provys.common.xsd;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Support class for xsd dateTime / date / time validation. Contains common code for handling timezone part of dateTime
 * information compliant with W3C XSD 1.1
 */
class XsTimezoneFormatter {
    /**
     * Class only contains static methods and properties
     */
    private XsTimezoneFormatter() {}

    /**
     * String defining format, accepted by STRICT formatter
     */
    static final String STRICT_REGEX = "(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))";

    /**
     * Formatter representing timezone
     */
    static final DateTimeFormatter STRICT = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .appendOffset("+HH:MM", "Z")
            .toFormatter();

    /**
     * Cache for formatters with given default time zone
     */
    private static final Map<String, DateTimeFormatter> ZONE_STRICT_MAP = new ConcurrentHashMap<>(1);

    /**
     * Return formatter with given default time zone offset
     */
    @Nonnull
    static DateTimeFormatter getStrict(String defOffset) {
        return ZONE_STRICT_MAP.computeIfAbsent(defOffset, defaultOffset -> new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .appendOffset("+HH:MM", defaultOffset)
                .toFormatter());
    }

    /**
     * String defining format, accepted by LENIENT formatter
     */
    static final String LENIENT_REGEX = "(Z|z|(\\+|-)((0?[0-9]|1[0-3])(:[0-5][0-9](:[0-5][0-9])?)?|14:00|1400|14))";

    /**
     * Formatter representing timezone
     */
    static final DateTimeFormatter LENIENT = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .appendOffset("+H:mm", "Z")
            .toFormatter();

    /**
     * Cache for formatters with given default time zone
     */
    private static final Map<String, DateTimeFormatter> ZONE_LENIENT_MAP = new ConcurrentHashMap<>(1);

    /**
     * Return formatter with given default time zone offset
     */
    @Nonnull
    static DateTimeFormatter getLenient(String defOffset) {
        return ZONE_LENIENT_MAP.computeIfAbsent(defOffset, defaultOffset -> new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .appendOffset("+H:mm", defaultOffset)
                .toFormatter());
    }
}
