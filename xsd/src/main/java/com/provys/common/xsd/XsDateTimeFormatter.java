package com.provys.common.xsd;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.*;

/**
 * Class provides formatter for parsing / formatting xs:dateTime values and their conversion to LocalDateTime used by
 * PROVYS system
 */
public final class XsDateTimeFormatter {

    /**
     * Class only contains static methods and properties
     */
    private XsDateTimeFormatter() {}

    /**
     * String defining format, accepted by STRICT formatter
     */
    public static final String STRICT_REGEX = XsDateFormatter.STRICT_DATE_REGEX + 'T' +
            XsTimeFormatter.STRICT_TIME_REGEX + XsTimezoneFormatter.STRICT_REGEX + '?';

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
            .append(XsDateFormatter.STRICT_DATE)
            .appendLiteral('T')
            .append(XsTimeFormatter.STRICT_TIME)
            .optionalStart()
            .append(XsTimezoneFormatter.STRICT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
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
            .append(XsDateFormatter.LENIENT_DATE)
            .appendText(NoValueField.DATE_TIME_DELIMITER, DATE_TIME_DELIMETER_MAP)
            .append(XsTimeFormatter.LENIENT_TIME)
            .optionalStart()
            .append(XsTimezoneFormatter.LENIENT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withChronology(IsoChronology.INSTANCE);

    public static final String LENIENT_REGEX = XsDateFormatter.LENIENT_DATE_REGEX + "[ Tt]" +
            XsTimeFormatter.LENIENT_TIME_REGEX + XsTimezoneFormatter.LENIENT_REGEX + '?';
    /**
     * Pattern that corresponds to strings, accepted by XSD_DATETIME_PARSER
     */
    public static final Pattern LENIENT_PATTERN = Pattern.compile(LENIENT_REGEX);

    private static final Map<String, DateTimeFormatter> XSD_DATETIME_PARSER_TZ = new ConcurrentHashMap<>(1);

    /**
     * Returns lenient parser that interprets missing timezone information as specified timezone
     */
    public static DateTimeFormatter getFormatterTZ(String defOffset) {
        Objects.requireNonNull(defOffset);
        return XSD_DATETIME_PARSER_TZ.computeIfAbsent(defOffset,
                defaultOffset -> new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(ISO_LOCAL_DATE)
                        .appendText(NoValueField.DATE_TIME_DELIMITER, DATE_TIME_DELIMETER_MAP)
                        .append(ISO_LOCAL_TIME)
                        .optionalStart()
                        .append(XsTimezoneFormatter.getLenient(defaultOffset))
                        .optionalEnd()
                        .toFormatter(Locale.US)
                        .withResolverStyle(ResolverStyle.STRICT)
                        .withChronology(IsoChronology.INSTANCE));
    }
}
