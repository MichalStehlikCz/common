package com.provys.common.xsd;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

/**
 * For parsing and formatting of {@code xs:date} values with validation strictly or loosely adhering to
 * <a href="https://www.w3.org/TR/xmlschema-2/#date">XML Schema</a>.
 */
public final class XsdDateFormatter {

    /**
     * Class only contains static methods and properties
     */
    private XsdDateFormatter() {}

    /**
     * String defining format, accepted by STRICT formatter
     */
    public static final String STRICT_REGEX = "-?[0-9]{1,4}-[0-1][0-9]-[0-3][0-9]";

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
            .append(ISO_LOCAL_DATE)
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

}
