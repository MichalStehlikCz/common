package com.provys.common.xsd;

import javax.annotation.Nonnull;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

/**
 * For parsing and formatting of {@code xs:date} values with validation strictly or loosely adhering to
 * <a href="https://www.w3.org/TR/xmlschema-2/#date">XML Schema</a>.
 */
public final class XsDateFormatter {

    /**
     * Class only contains static methods and properties
     */
    private XsDateFormatter() {}

    /**
     * String defining date part of format, accepted by STRICT formatter
     */
    static final String STRICT_DATE_REGEX = "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";

    /**
     * Formatter that parses date part
     */
    static final DateTimeFormatter STRICT_DATE = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();

    /**
     * String defining format, accepted by STRICT formatter
     */
    public static final String STRICT_REGEX = STRICT_DATE_REGEX + XsTimezoneFormatter.STRICT_REGEX + '?';

    /**
     * Pattern that corresponds to strings, accepted by STRICT formatter
     */
    public static final Pattern STRICT_PATTERN = Pattern.compile(STRICT_REGEX);

    /**
     * For parsing and formatting of {@code xs:date} values with validation strictly adhering to
     * <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">XML Schema</a>.
     */
    public static final DateTimeFormatter STRICT = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .append(STRICT_DATE)
            .optionalStart()
            .append(XsTimezoneFormatter.STRICT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    /**
     * String defining date part of format, accepted by STRICT formatter
     */
    static final String LENIENT_DATE_REGEX = "-?([1-9][0-9]{3,}|0[0-9]{3}|[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";

    /**
     * Formatter parsing date part, lenient
     */
    @Nonnull
    static final DateTimeFormatter LENIENT_DATE = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 2, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();

    /**
     * String defining format, accepted by LENIENT formatter
     */
    public static final String LENIENT_REGEX = LENIENT_DATE_REGEX + XsTimezoneFormatter.LENIENT_REGEX + '?';

    /**
     * Pattern that corresponds to strings, accepted by STRICT formatter
     */
    public static final Pattern LENIENT_PATTERN = Pattern.compile(LENIENT_REGEX);

    /**
     * For parsing and formatting of {@code xs:date} values with validation strictly adhering to
     * <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">XML Schema</a>.
     */
    public static final DateTimeFormatter LENIENT = new DateTimeFormatterBuilder()
            .append(LENIENT_DATE)
            .optionalStart()
            .append(XsTimezoneFormatter.LENIENT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

}
