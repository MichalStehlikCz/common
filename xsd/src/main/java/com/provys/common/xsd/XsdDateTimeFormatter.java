package com.provys.common.xsd;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.*;

/**
 * Class provides formatter for parsing / formatting xs:dateTime values and their conversion to LocalDateTime used by
 * PROVYS system
 */
public final class XsdDateTimeFormatter {

    private static final String XSD_TZ_OFFSET_PATTERN_STRICT = "+HH:MM";
    private static final String XSD_TZ_OFFSET_PATTERN_LENIENT = "+HH:mm";

    /**
     * Class only contains static methods and properties
     */
    private XsdDateTimeFormatter() {}

    /**
     * String defining format, accepted by STRICT formatter
     */
    public static final String STRICT_REGEX = XsdDateFormatter.STRICT_REGEX +
            "T[0-2][0-9]:[0-5][0-9]:[0-5][0-9](?:\\.[0-9]{1,9})?(?:Z|(?:[-+][0-2][0-9]:[0-5][0-9]))?";

    /**
     * Pattern that corresponds to strings, accepted by STRICT formatter
     */
    public static final Pattern STRICT_PATTERN = Pattern.compile(STRICT_REGEX);

    /**
     * For parsing and formatting of {@code xs:dateTime} values with validation strictly adhering to
     * <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">XML Schema</a>.
     */
    public static final DateTimeFormatter STRICT = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .optionalEnd()
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_STRICT, "Z")
                .optionalEnd()
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);

    /**
     * Defines acceptable values for delimiter between date and time
     */
    private static final Map<Long, String> DATE_TIME_DELIMETER_MAP = new HashMap<>(2);
    static {
        DATE_TIME_DELIMETER_MAP.put(1L, "T");
        DATE_TIME_DELIMETER_MAP.put(2L, " ");
    }

    /**
     * Lenient formatter that can be used for parsing {@code xs:dateTime} values with less strict validation.
     * Parser works in following situations that do not adhere to standard:
     * <ul>
     *   <li>In the time value the seconds field may be omitted. Hence {@code "2019-03-12T14:45Z"} is an acceptable
     *   value. The formal specification mandates that the seconds field is present, even if zero.</li>
     *   <li>The delimiter between the date and the time value may be a {@code 'T'} or
     *       a {@code ' '} (space). The formal definition only allows a {@code 'T'}. </li>
     *   <li>Literals in the string, i.e. {@code 'T'} and {@code 'Z'}, are accepted regardless
     *       of upper/lower-case. The formal specification mandates that these are
     *       in upper-case.</li>
     *   <li>If a time zone offset is specified it doesn't have to include minutes,
     *       meaning that {@code "+05"} is an acceptable value for an offset.
     *       The formal definition mandates that the minutes <i>must</i> be specified,
     *       i.e. {@code "+05:00"}. </li>
     *   <li>Trailing zeroes in the fractional second is acceptable,
     *       meaning that a value such as {@code "2019-03-12T14:45:28.340Z"} will
     *       be accepted. The formal specification says "the fractional second
     *       string, if present, must not end in {@code '0'}".</li>
     * </ul>
     */
    public static final DateTimeFormatter LENIENT = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATE_TIME_DELIMETER_MAP)
                .append(ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_LENIENT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);

    /**
     * Pattern that corresponds to strings, accepted by XSD_DATETIME_PARSER
     */
    public static final Pattern LENIENT_PATTERN = Pattern.compile(
            "-?[0-9]{1,4}-[0-1][0-9]-[0-3][0-9][ T][0-2][0-9]:[0-5][0-9](?::[0-5][0-9](?:\\.[0-9]{1,9})?)?(?:Z|(?:[-+][0-2][0-9](?::?[0-5][0-9])?))?");

    private static final Map<String, DateTimeFormatter> XSD_DATETIME_PARSER_TZ = new ConcurrentHashMap<>(1);

    /**
     * Returns lenient parser that interprets missing timezone information as specified timezone
     */
    public static DateTimeFormatter getFormatterTZ(String noOffsetText) {
        return XSD_DATETIME_PARSER_TZ.computeIfAbsent(noOffsetText, defaultOffset -> new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATE_TIME_DELIMETER_MAP)
                .append(ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_LENIENT, defaultOffset)
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE));
    }
}
