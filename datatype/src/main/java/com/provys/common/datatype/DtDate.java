package com.provys.common.datatype;

import com.provys.common.exception.InternalException;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Implements support for standard Provys DATE domain. DtDate value is immutable.
 * DATE values are held in DtDate instances in Provys Java framework; at the moment logic is based on JDK's
 * {@code LocalDate} functionality, but this behaviour can change later.
 */
@SuppressWarnings("WeakerAccess")
@JsonbTypeAdapter(JsonbDtDateAdapter.class)
@XmlJavaTypeAdapter(XmlDtDateAdapter.class)
public final class DtDate implements Comparable<DtDate> {

    /**
     * Minimal value that can be represented by DtDate; all special values must fall into this range
     */
    private static final LocalDate MINVALUE = LocalDate.of(1000, 1, 1);

    /**
     * Maximal value that can be represented by DtDate; all special values must fall to this range
     */
    private static final LocalDate MAXVALUE = LocalDate.of(5000, 1, 1);

    /**
     * Date value, returned when user doesn't have the rights to access the value
     */
    public static final DtDate PRIV = of(1000, 1, 2);

    /**
     * Date value, returned as indication of multi-value
     */
    public static final DtDate ME = of(1000, 1, 1);

    /**
     * Minimal date value, valid in Provys
     */
    public static final DtDate MIN = of(1000, 1, 3);

    /**
     * Maximal date value, valid in Provys
     */
    public static final DtDate MAX = of(5000, 1, 1);

    /**
     * Text representing PRIV value
     */
    public static final String PRIV_TEXT = DtString.PRIV;

    /**
     * Text representing ME value
     */
    public static final String ME_TEXT = DtString.ME;

    /**
     * Text representing MIN value
     */
    public static final String MIN_TEXT = "<<<<<<<<";

    /**
     * Text representing MAX value
     */
    public static final String MAX_TEXT = ">>>>>>>>";

    /**
     * Regular expression validating year, strict (4 digit). Valid years correspond to date values allowed in Provys
     * framework
     */
    public static final String YEAR_REGEX_STRICT = "[1-5][0-9]{3}";

    /**
     * Regular expression validating month, strict (2 digit)
     */
    public static final String MONTH_REGEX_STRICT = "(?:0[1-9]|1[0-2])";

    /**
     * Regular expression validating day, strict (2 digits)
     */
    public static final String DAY_REGEX_STRICT = "(?:0[1-9]|[1-2][0-9]|3[0-1])";

    /**
     * Defines format, supported by Iso parse
     */
    public static final Pattern ISO_DATE_STRICT = Pattern.compile("(" + YEAR_REGEX_STRICT + ")-(" + MONTH_REGEX_STRICT +
            ")-(" + DAY_REGEX_STRICT + ")");

    /**
     * Regular expression validating year, lenient; allows 2 digit year (that is then interpreted in -50/+50 years
     * window)
     */
    public static final String YEAR_REGEX_LENIENT = "(?:" + YEAR_REGEX_STRICT + "|[0-9]{2})";

    /**
     * Regular expression validating month, lenient (supports 1 or 2 digits)
     */
    public static final String MONTH_REGEX_LENIENT = "(?:0?[1-9]|1[0-2])";

    /**
     * Regular expression validating day, lenient (1-2 digits)
     */
    public static final String DAY_REGEX_LENIENT = "(?:0?[1-9]|[1-2][0-9]|3[0-1])";

    /**
     * Defines format, supported by Iso parse
     */
    public static final Pattern ISO_DATE_LENIENT = Pattern.compile("(" + YEAR_REGEX_LENIENT + ")[-.](" + MONTH_REGEX_LENIENT +
            ")[-.](" + DAY_REGEX_LENIENT + ")(?:00:00(?::00(?:[,.][0]{0,9})))");

    /**
     * Retrieves instance of {@code DtDate} corresponding to given {@code LocalDate}.
     *
     * @param value is {@code LocalDate} value to be represented by this date
     * @return converted value
     */
    public static DtDate ofLocalDate(LocalDate value) {
        return new DtDate(value);
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day.
     *
     * @param year is year of date to be created
     * @param month is calendar month of date to be created
     * @param day is day in month of date to be created
     * @return date value corresponding to supplied data
     */
    public static DtDate of(int year, short month, short day) {
        return new DtDate(LocalDate.of(year, month, day));
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day.
     *
     * @param year is year of date to be created
     * @param month is calendar month of date to be created
     * @param day is day in month of date to be created
     * @param allowSpecial indicates if special date values can be returned from this function
     * @return date value corresponding to supplied data
     */
    public static DtDate of(int year, short month, short day, boolean allowSpecial) {
        if (allowSpecial) {
            if ((year == PRIV.value.getYear()) && (month == PRIV.value.getMonthValue()) &&
                    (day == PRIV.value.getDayOfMonth())) {
                return PRIV;
            }
            if ((year == ME.value.getYear()) && (month == ME.value.getMonthValue()) &&
                    (day == ME.value.getDayOfMonth())) {
                return ME;
            }
            if ((year == MIN.value.getYear()) && (month == MIN.value.getMonthValue()) &&
                    (day == MIN.value.getDayOfMonth())) {
                return MIN;
            }
            if ((year == MAX.value.getYear()) && (month == MAX.value.getMonthValue()) &&
                    (day == MAX.value.getDayOfMonth())) {
                return MAX;
            }
        }
        return of(year, month, day);
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day as {@code int} values.
     *
     * @param year is year of date to be created
     * @param month is calendar month of date to be created
     * @param day is day in month of date to be created
     * @return date value corresponding to supplied data
     */
    public static DtDate of(int year, int month, int day) {
        return DtDate.of(year, (short) month, (short) day);
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day.
     *
     * @param year is year of date to be created
     * @param month is calendar month of date to be created
     * @param day is day in month of date to be created
     * @param allowSpecial indicates if special date values can be returned from this function
     * @return date value corresponding to supplied data
     */
    public static DtDate of(int year, int month, int day, boolean allowSpecial) {
        return of(year, (short) month, (short) day, allowSpecial);
    }

    /**
     * Retrieve instance of {@code DtDate} corresponding to current date (in default time-zone).
     *
     * @return date value corresponding to current date
     */
    public static DtDate now() {
        return ofLocalDate(LocalDate.now());
    }

    /**
     * Retrieve instance of {@code DtDate} based on instant and time zone.
     *
     * @param instant is instant new value represents
     * @param zone is timezone instant is considered to be in
     * @return value representing given instant
     */
    public static DtDate ofInstant(Instant instant, ZoneId zone) {
        return ofLocalDate(LocalDate.ofInstant(instant, zone));
    }

    /**
     * Retrieve instance of {@code DtDate} based on instant, in default time zone.
     *
     * @param instant is instant new value represents
     * @return value representing given instant
     */
    public static DtDate ofInstant(Instant instant) {
        return ofLocalDate(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
    }

    /**
     * Parse value from StringParser; unlike "normal" String parse, this method can read only part of parser content;
     * parser is moved after last character read as part of date values.
     *
     * @param parser is parser containing text to be read
     * @param allowSpecialText defines if parser should recognise special texts (indicating special values)
     * @param allowSpecialValue defines if special values should be possible to parse (in regular date format); ignored
     *                         if allowSpecialText is set and position is on special value's textual representation
     * @return datetime value read from parser
     */
    public static DtDate parse(StringParser parser, boolean allowSpecialText, boolean allowSpecialValue) {
        if (!parser.hasNext()) {
            throw new DateTimeParseException("Empty parser supplied to read DtDate", parser.getString(),
                    parser.getPos());
        }
        if (allowSpecialText) {
            if (parser.onText(PRIV_TEXT)) {
                return PRIV;
            }
            if (parser.onText(ME_TEXT)) {
                return ME;
            }
            if (parser.onText(MIN_TEXT)) {
                return MIN;
            }
            if (parser.onText(MAX_TEXT)) {
                return MAX;
            }
        }
        try {
            var year = parser.readUnsignedInt(4);
            if (parser.next() != '-') {
                throw new DateTimeParseException("Expecting - as year / month delimiter", parser.getString(),
                        parser.getPos());
            }
            var month = parser.readUnsignedInt(2);
            if (parser.next() != '-') {
                throw new DateTimeParseException("Expecting - as year / month delimiter", parser.getString(),
                        parser.getPos());
            }
            var day = parser.readUnsignedInt(2);
            return of(year, month, day, allowSpecialValue);
        } catch (NoSuchElementException | StringIndexOutOfBoundsException e) {
            throw new DateTimeParseException("Unexpected end of string encountered", parser.getString(),
                    parser.getPos());
        } catch (InternalException e) {
            throw new DateTimeParseException(e.getMessage(), parser.getString(), parser.getPos(), e);
        }
    }

    /**
     * Parse provided text in strict ISO local date format. Also supports parsing special values.
     *
     * @param text is text in ISO-8601 format for local date (e.g. YYYY-MM-DD)
     * @return date value corresponding to provided text
     */
    public static DtDate parse(String text) {
        var parser = new StringParser(text);
        var result = parse(parser, true, false);
        if (parser.hasNext()) {
            throw new DateTimeParseException("Value parsed before reading whole text", text, parser.getPos());
        }
        return result;
    }

    private static DtDate parseIsoTime(DtDate date, StringParser parser) {
        var result = date;
        if (parser.hasNext() && (parser.peek() == 'T')) {
            parser.next();
            // parse optional zero time part
            int hours = parser.readUnsignedInt(2);
            if (hours == 24) {
                // 24 hours is supported as end of day... it represents following day
                result = result.plusDays(1);
            } else if (hours != 0) {
                throw new DateTimeParseException("Hours expected to equal zero in ISO date", parser.toString(),
                        parser.getPos());
            }
            if (parser.next() != ':') {
                throw new DateTimeParseException(": expected as hour - minute delimiter in ISO date",
                        parser.getString(), parser.getPos());
            }
            if (parser.readUnsignedInt(2) != 0) {
                throw new DateTimeParseException("Minutes expected to equal zero in ISO date", parser.toString(),
                        parser.getPos());
            }
            if (parser.hasNext() && (parser.peek() == ':')) {
                // seconds
                parser.next();
                if (parser.readUnsignedInt(2) != 0) {
                    throw new DateTimeParseException("Seconds expected to equal zero in ISO date", parser.toString(),
                            parser.getPos());
                }
                if (parser.hasNext() && ((parser.peek() == '.') || (parser.peek() == ','))) {
                    // fractional part of seconds
                    parser.next();
                    if (parser.readUnsignedInt(1, 18) != 0) {
                        throw new DateTimeParseException("Milliseconds expected to equal zero in ISO date",
                                parser.toString(), parser.getPos());
                    }
                }
            }
        }
        return result;
    }

    private static void parseIsoTimeZone(StringParser parser) {
        if (parser.hasNext() && (parser.peek() == 'Z')) {
            // UTC time
            parser.next();
        } else if (parser.hasNext() && ((parser.peek() == '+') || (parser.peek() == '-'))) {
            // timezone
            parser.next();
            parser.readUnsignedInt(2);
            if (parser.hasNext() && (parser.peek() == ':')) {
                parser.next();
                parser.readUnsignedInt(2);
            }
        }
    }

    /**
     * Parse provided text in ISO local date format (lenient); special values are encoded as regular date strings
     *
     * @param parser is parser containing text in ISO-8601 format for local date (e.g. YYYY-MM-DD); also accepts date
     *              with zero time or with zero time with timezone
     * @param allowTime defines if time following date information is to be parsed. If so, it parses time and throws
     *                 exception if it is different from zero or 24 hours
     * @return datetime value read from parser
     */
    public static DtDate parseIso(StringParser parser, boolean allowTime) {
        if (!parser.hasNext()) {
            throw new DateTimeParseException("Empty parser supplied to read DtDate", parser.getString(),
                    parser.getPos());
        }
        try {
            var result = parse(parser, false, true);
            if (allowTime) {
                result = parseIsoTime(result, parser);
                parseIsoTimeZone(parser);
            }
            return result;
        } catch (NoSuchElementException | StringIndexOutOfBoundsException e) {
            throw new DateTimeParseException("Unexpected end of string encountered in ISO date", parser.getString(),
                    parser.getPos());
        }
    }

    /**
     * Parse provided text in ISO local date format (lenient); special values are encoded as regular date strings
     *
     * @param text is text in ISO-8601 format for local date (e.g. YYYY-MM-DD); also accepts date with zero time
     *             or with zero time with timezone
     * @return date value corresponding to provided text
     */
    public static DtDate parseIso(String text) {
        var parser = new StringParser(text);
        var result = parseIso(parser, true);
        if (parser.hasNext()) {
            throw new DateTimeParseException("ISO date value parsed before reading whole text", text, parser.getPos());
        }
        return result;
    }

    /**
     * Parse provided text as Provys string representation of date value, in format DD.MM.YYYY. It also accepts value
     * with time part 00:00:00. It can finish reading before reading whole content of parser.
     *
     * @param parser is parser containing value in Provys string representation
     * @return date value corresponding to provided text
     */
    public static DtDate ofProvysValue(StringParser parser) {
        try {
            var day = parser.readUnsignedInt(2);
            if (parser.next() != '.') {
                throw new DateTimeParseException("Invalid Provys date string format delimiter " + parser.current(),
                        parser.getString(), 2);
            }
            var month = parser.readUnsignedInt(2);
            if (parser.next() != '.') {
                throw new DateTimeParseException("Invalid Provys date string format delimiter " + parser.current(),
                        parser.getString(), 2);
            }
            var year = parser.readUnsignedInt(4);
            // only time " 00:00:00" is allowed to be present for date values - and if it is, it is read
            parser.onText(" 00:00:00");
            return of(year, month, day, true);
        } catch (NoSuchElementException e) {
            throw new DateTimeParseException("String finished before reading whole value", parser.getString(),
                    parser.getPos(), e);
        } catch (InternalException e) {
            throw new DateTimeParseException("Error reading date value", parser.getString(), parser.getPos(), e);
        } catch (DateTimeException e) {
            throw new DateTimeParseException("Parsed value is not valid date value", parser.getString(),
                    parser.getPos(), e);
        }
    }

    /**
     * Parse provided text as Provys string representation of date value, in format DD.MM.YYYY. It also accepts value
     * with time part 00:00:00.
     *
     * @param value is value in Provys string representation
     * @return date value corresponding to provided text
     */
    public static DtDate ofProvysValue(String value) {
        var parser = new StringParser(Objects.requireNonNull(value));
        var result = DtDate.ofProvysValue(parser);
        if (parser.hasNext()) {
            throw new DateTimeParseException("Time parsed before reading whole supplied value", value, parser.getPos());
        }
        return result;
    }

    /**
     * Actual date represented by this DtDate object
     */
    private final LocalDate value;

    /**
     * Constructor; only used internally, use static methods to retrieve {@code DtDate} instances
     *
     * @param value is value DtDate object shall be assigned
     */
    private DtDate(LocalDate value) {
        Objects.requireNonNull(value);
        if (value.compareTo(MINVALUE) < 0) {
            throw new DateTimeException(value.toString() + " is not valid date - cannot be smaller than " + MINVALUE);
        }
        if (value.compareTo(MAXVALUE) > 0) {
            throw new DateTimeException(value.toString() + " is not valid date - cannot be bigger than " + MINVALUE);
        }
        this.value = value;
    }

    /**
     * Indicates if given value is regular date value. Regular values are valid values in period MIN ... MAX
     * (exclusive)
     *
     * @return true if date is inside interval MIN - MAX, false if given date is special value (PRIV, ME, MIN, MAX)
     */
    public boolean isRegular() {
        return (compareTo(MIN) > 0) && (compareTo(MAX) < 0);
    }

    /**
     * Indicates values that are valid as date values in Provys. Note that boundary values (MIN, MAX) might be valid
     * only for some properties and not valid for others.
     *
     * @return true if this value is regular, MIN or MAX
     */
    public boolean isValidValue() {
        return (compareTo(MIN) >= 0) && (compareTo(MAX) <= 0);
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

    /**
     * @return if this value is MIN (start of unlimited interval)
     */
    public boolean isMin() {
        return equals(MIN);
    }

    /**
     * @return if this value is MAX (end of unlimited interval)
     */
    public boolean isMax() {
        return equals(MAX);
    }

    /**
     * @return {@code LocalDate} represented by this object
     */
    public LocalDate getLocalDate() {
        return value;
    }

    /**
     * @return year from this date value
     */
    public int getYear() {
        if (this.equals(PRIV)) {
            return DtInteger.PRIV;
        }
        if (this.equals(ME)) {
            return DtInteger.ME;
        }
        return value.getYear();
    }

    /**
     * @return month from this date value
     */
    public int getMonthValue() {
        if (this.equals(PRIV)) {
            return DtInteger.PRIV;
        }
        if (this.equals(ME)) {
            return DtInteger.ME;
        }
        return value.getMonthValue();
    }

    /**
     * @return day of month from this date value
     */
    public int getDayOfMonth() {
        if (this.equals(PRIV)) {
            return DtInteger.PRIV;
        }
        if (this.equals(ME)) {
            return DtInteger.ME;
        }
        return value.getDayOfMonth();
    }

    /**
     * Returns a copy of this {@code DtDate} with the specified number of days added.
     * This method adds the specified amount of days and ensures result remains valid. The result is only invalid if the
     * maximum/minimum year is exceeded. When function is applied on special value (MIN, MAX, PRIV, ME), it is returned
     * unchanged. If supplied integer value corresponds to one of special values (PRIV, ME), corresponding special value
     * is returned
     *
     * @param daysToAdd  the days to add, may be negative
     * @return a {@code DtDate} based on this date with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public DtDate plusDays(int daysToAdd) {
        if (equals(PRIV) || DtInteger.PRIV.equals(daysToAdd)) {
            return PRIV;
        }
        if (equals(ME) || DtInteger.ME.equals(daysToAdd)) {
            return ME;
        }
        if (equals(MIN) || equals(MAX)) {
            return this;
        }
        if (DtInteger.MIN.equals(daysToAdd)) {
            return MIN;
        }
        if (DtInteger.MAX.equals(daysToAdd)) {
            return MAX;
        }
        if (daysToAdd == 0) {
            return this;
        }
        return ofLocalDate(getLocalDate().plusDays(daysToAdd));
    }

    /**
     * Difference of dates (in days). Note that in case this or operand are special values, you will not get original
     * value when applying result of minus on operand
     *
     * @param date date to be subtracted
     * @return different of supplied dates in days. Returns PRIV if any of operands if missing privileges value, ME if
     * any of the operands is ME and not PRIV
     */
    public int minus(DtDate date) {
        if (isPriv() || date.isPriv()) {
            return DtInteger.PRIV;
        }
        if (isME() || date.isME()) {
            return DtInteger.ME;
        }
        if (isMax()) {
            return date.isMax() ? 0 : DtInteger.MAX;
        }
        if (isMin()) {
            return date.isMin() ? 0 : DtInteger.MIN;
        }
        if (date.isMax()) {
            return DtInteger.MIN;
        }
        if (date.isMin()) {
            return DtInteger.MAX;
        }
        return (int) ChronoUnit.DAYS.between(date.getLocalDate(), this.getLocalDate());
    }

    /**
     * Converts {@code DtDate} value to ISO date string representation. Unlike toString, special values are also
     * converted to normal Iso date format, as special strings would not pass document validation
     *
     * @return string representation of this value in ISO format
     */
    public String toIso() {
        if (isPriv()) {
            return "1000-01-02";
        }
        if (isME()) {
            return "1000-01-01";
        }
        if (isMin()) {
            return "1000-01-03";
        }
        if (isMax()) {
            return "5000-01-01";
        }
        return toString();
    }

    /**
     * Converts {@code DtDate} value to Provys string representation (format DD.MM.YYYY)
     *
     * @return string representation of this value in PROVYS date format
     */
    public String toProvysValue() {
        return String.format((Locale) null, "%02d", value.getDayOfMonth()) + '.' +
                String.format((Locale) null, "%02d", value.getMonthValue()) + '.' +
                String.format((Locale) null, "%04d", value.getYear());
    }

    /**
     * DtDate values are equal when their corresponding LocalDate value is equal.
     *
     * @param o is other object to be compared to
     * @return true / false indicating if two dates are the same
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DtDate that = (DtDate) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(DtDate o) {
        return value.compareTo(o.getLocalDate());
    }

    /**
     * @return ISO-8601 representation of date, represented by this object
     */
    @Override
    public String toString() {
        if (equals(PRIV)) {
            return PRIV_TEXT;
        }
        if (equals(ME)) {
            return ME_TEXT;
        }
        if (equals(MIN)) {
            return MIN_TEXT;
        }
        if (equals(MAX)) {
            return MAX_TEXT;
        }
        return value.toString();
    }
}
