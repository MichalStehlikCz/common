package com.provys.common.datatype;

import javax.annotation.Nonnull;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;

/**
 * Implements support for standard Provys DATE domain. DtDate value is immutable.
 * DATE values are held in DtDate instances in Provys Java framework; at the momentm logic is based on JDK's
 * {@code LocalDate} functionality, but this behaviour can change later.
 */
final public class DtDate implements Comparable<DtDate> {

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
     * Retrieves instance of {@code DtDate} corresponding to given {@code LocalDate}.
     *
     * @param value is {@code LocalDate} value to be represented by this date
     */
    @Nonnull
    public static DtDate ofLocalDate(LocalDate value) {
        return new DtDate(value);
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day.
     */
    @Nonnull
    public static DtDate of(int year, short month, short day) {
        return new DtDate(LocalDate.of(year, month, day));
    }

    /**
     * Retrieves instance of {@code DtDate} corresponding to given year, month and day as {@code int} values.
     */
    @Nonnull
    public static DtDate of(int year, int month, int day) {
        return DtDate.of(year, (short) month, (short) day);
    }

    /**
     * Retrieve instance of {@code DtDate} corresponding to current date (in default time-zone).
     */
    @Nonnull
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
    @Nonnull
    public static DtDate ofInstant(Instant instant, ZoneId zone) {
        return ofLocalDate(LocalDate.ofInstant(instant, zone));
    }

    /**
     * Retrieve instance of {@code DtDate} based on instant, in default time zone.
     *
     * @param instant is instant new value represents
     * @return value representing given instant
     */
    @Nonnull
    public static DtDate ofInstant(Instant instant) {
        return ofLocalDate(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
    }

    /**
     * Parse provided text in strict ISO local date format.
     *
     * @param text is text in ISO-8601 format for local date (e.g. YYYY-MM-DD)
     * @return date value corresponding to provided text
     */
    @Nonnull
    public static DtDate parse(CharSequence text) {
        return ofLocalDate(LocalDate.parse(text));
    }

    /**
     * Parse provided text as Provys string representation of date value, in format DD.MM.YYYY. It also accepts value
     * with time part 00:00:00.
     *
     * @param value is value in Provys string represetation
     * @return date value corresponding to provided text
     */
    @Nonnull
    public static DtDate ofProvysValue(String value) {
        var parser = new StringParser(Objects.requireNonNull(value));
        var day = parser.readUnsignedInt(2);
        if (parser.next() != '.') {
            throw new DateTimeParseException("Invalid Provys date string format delimiter " + parser.current(), value,
                    2);
        }
        var month = parser.readUnsignedInt(2);
        if (parser.next() != '.') {
            throw new DateTimeParseException("Invalid Provys date string format delimiter " + parser.current(), value,
                    2);
        }
        var year = parser.readUnsignedInt(4);
        // only time " 00:00:00" is allowed to be present for date values
        if (parser.hasNext() && !value.substring(10).equals(" 00:00:00")) {
            throw new DateTimeParseException("Only date or date with 00:00:00 allowed", value, 10);
        }
        return of(year, month, day);
    }

    /**
     * Actual date represented by this DtDate object
     */
    @Nonnull
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
     * @return {@code LocalDate} represented by this object
     */
    public LocalDate getLocalDate() {
        return value;
    }

    /**
     * @return year from this date value
     */
    public int getYear() {
        return value.getYear();
    }

    /**
     * @return month from this date value
     */
    public int getMonthValue() {
        return value.getMonthValue();
    }

    /**
     * @return day of month from this date value
     */
    public int getDayOfMonth() {
        return value.getDayOfMonth();
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
    public boolean isValid() {
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
    @Nonnull
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
     * Converts {@code DtDate} value to Provys string representation (format DD.MM.YYYY)
     */
    @Nonnull
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
    public boolean equals(Object o) {
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
    @Nonnull
    @Override
    public String toString() {
        return value.toString();
    }
}
