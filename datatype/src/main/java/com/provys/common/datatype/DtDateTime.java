package com.provys.common.datatype;

import javax.annotation.Nonnull;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DtDateTime implements Comparable<DtDateTime> {

    /**
     * Date value, returned when user doesn't have the rights to access the value
     */
    public static final DtDateTime PRIV = new DtDateTime(DtDate.PRIV, DtTimeS.ofSeconds(0));

    /**
     * Date value, returned as indication of multi-value
     */
    public static final DtDateTime ME = new DtDateTime(DtDate.ME, DtTimeS.ofSeconds(0));

    /**
     * Minimal date value, valid in Provys
     */
    public static final DtDateTime MIN = new DtDateTime(DtDate.MIN, DtTimeS.ofSeconds(0));

    /**
     * Maximal date value, valid in Provys
     */
    public static final DtDateTime MAX =  new DtDateTime(DtDate.MAX, DtTimeS.ofSeconds(0));

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
     * Creates DtDateTime for supplied date and time.
     *
     * @param year is year of the date
     * @param month is month of the date
     * @param day is day in month
     * @param hour is hour (0-23)
     * @param minute is minute (0-59)
     * @param second is second (0-59)
     */
    @Nonnull
    public static DtDateTime of(int year, int month, int day, int hour, int minute, int second) {
        return ofDateTime(DtDate.of(year, month, day), DtTimeS.ofHourToSecond(hour, minute, second));
    }

    /**
     * Creates DtDateTime for supplied date and time.
     *
     * @param year is year of the date
     * @param month is month of the date
     * @param day is day in month
     * @param hour is hour (0-23)
     * @param minute is minute (0-59)
     */
    @Nonnull
    public static DtDateTime of(int year, int month, int day, int hour, int minute) {
        return of(year, month, day, hour, minute, 0);
    }

    /**
     * Creates DtDateTime for supplied date.
     *
     * @param year is year of the date
     * @param month is month of the date
     * @param day is day in month
     */
    @Nonnull
    public static DtDateTime of(int year, int month, int day) {
        return of(year, month, day, 0, 0);
    }

    /**
     * Creates DtDateTime value based on supplied date.
     *
     * @param date is date value; time will be set to 0
     */
    @Nonnull
    public static DtDateTime ofDate(DtDate date) {
        if (date.isPriv()) {
            return PRIV;
        }
        if (date.isME()) {
            return ME;
        }
        if (date.isMin()) {
            return MIN;
        }
        if (date.isMax()) {
            return MAX;
        }
        return new DtDateTime(date, DtTimeS.ofSeconds(0));
    }

    /**
     * Creates DtDateTime value based on supplied date and time values. If date or time are PRIV values, PRIV value is
     * returned. If date or time are ME values, ME value is returned. If date is MIN or MAX value, MIN or MAX value is
     * returned; if time is MIN or MAX value and date is not, exception is thrown
     *
     * @param date is date value
     * @param time is time value; it can be outside 0-24h interval, days are moved to date part and datetime is stored
     *             in canonical form
     */
    @Nonnull
    public static DtDateTime ofDateTime(DtDate date, DtTimeS time) {
        if (date.isPriv() || time.isPriv()) {
            return PRIV;
        }
        if (date.isME() || time.isME()) {
            return ME;
        }
        if (date.isMin()) {
            if (time.isMax()) {
                throw new DateTimeException("Cannot create datetime from min date and max time");
            }
            return MIN;
        }
        if (date.isMax()) {
            if (time.isMin()) {
                throw new DateTimeException("Cannot create datetime from max date and min time");
            }
            return MAX;
        }
        // both used functions return this when days part of time is zero... so no need for optimisation here
        return new DtDateTime(date.plusDays(time.getDays()), time.getTime24());
    }

    /** Date part of datetime value */
    @Nonnull
    private final DtDate date;
    /** Time part of datetime value; valid values between 0-24 hours */
    @Nonnull
    private final DtTimeS time;

    /**
     * Private constructor, creates DtDateTime value from supplied date and time.
     *
     * @param date - date value
     * @param time - time value; must be 0 for special values and 0-24 for regular dates
     */
    @Nonnull
    private DtDateTime(DtDate date, DtTimeS time) {
        if (!date.isRegular()) {
            if (time.getSeconds() != 0) {
                throw new DateTimeException("Time part of irregular date-time must be zero");
            }
        } else {
            if (time.getDays() != 0) {
                throw new DateTimeException("Time part of date-time value must be between 0-24h");
            }
        }
        this.date = Objects.requireNonNull(date);
        this.time = Objects.requireNonNull(time);
    }

    /**
     * Retrieve instance of {@code DtDateTime} corresponding to given LocalDateTime value
     */
    @Nonnull
    public static DtDateTime ofLocalDateTime(LocalDateTime dateTime) {
        return new DtDateTime(DtDate.ofLocalDate(dateTime.toLocalDate()), DtTimeS.ofLocalTime(dateTime.toLocalTime()));
    }

    /**
     * Retrieve instance of {@code DtDateTime} corresponding to current date and time (in default time-zone).
     */
    @Nonnull
    public static DtDateTime now() {
        // via LocalDateTime to ensure date and time part correspond to same moment
        return ofLocalDateTime(LocalDateTime.now());
    }

    /**
     * Retrieve instance of {@code DtDateTime} based on instant and time zone.
     *
     * @param instant is instant new value represents
     * @param zone is timezone instant is considered to be in
     * @return value representing given instant
     */
    @Nonnull
    public static DtDateTime ofInstant(Instant instant, ZoneId zone) {
        return ofLocalDateTime(LocalDateTime.ofInstant(instant, zone));
    }

    /**
     * Retrieve instance of {@code DtDateTime} based on instant, in default time zone.
     *
     * @param instant is instant new value represents
     * @return value representing given instant
     */
    @Nonnull
    public static DtDateTime ofInstant(Instant instant) {
        return ofLocalDateTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    /**
     * Parse value from StringParser; unlike "normal" String parse, this method can read only part of parser content;
     * parser is moved after last character read as part of date values.
     *
     * @param parser is parser containing text to be read
     * @return datetime value read from parser
     */
    @Nonnull
    public static DtDateTime parse(StringParser parser) {
        if (!parser.hasNext()) {
            throw new DateTimeParseException("Empty parser supplied to read DtDate", parser.getString(),
                    parser.getPos());
        }
        if (parser.onText(PRIV_TEXT)) {
            return DtDateTime.PRIV;
        }
        if (parser.onText(ME_TEXT)) {
            return DtDateTime.ME;
        }
        if (parser.onText(MIN_TEXT)) {
            return DtDateTime.MIN;
        }
        if (parser.onText(MAX_TEXT)) {
            return DtDateTime.MAX;
        }
        var date = DtDate.parse(parser, false, false);
        if (parser.next() != 'T') {
            throw new DateTimeParseException("T expected as delimiter of date and time part", parser.getString(),
                    parser.getPos());
        }
        var time = DtTimeS.parse(parser, false, false, false);
        return ofDateTime(date, time);
    }

    /**
     * Parse provided text in strict ISO local date format; also supports parsing special values.
     *
     * @param text is text in ISO-8601 format for local date (e.g. YYYY-MM-DD)
     * @return date value corresponding to provided text
     */
    @Nonnull
    public static DtDateTime parse(String text) {
        var parser = new StringParser(text);
        var result = DtDateTime.parse(parser);
        if (parser.hasNext()) {
            throw new DateTimeParseException("Value parsed before reading whole text", text, parser.getPos());
        }
        return result;
    }

    /**
     * @return value of field date
     */
    @Nonnull
    public DtDate getDate() {
        return date;
    }

    /**
     * @return value of field time
     */
    @Nonnull
    public DtTimeS getTime() {
        if (isPriv()) {
            return DtTimeS.PRIV;
        }
        if (isME()) {
            return DtTimeS.ME;
        }
        return time;
    }

    /**
     * Get time, but based on specified base date (e.g. potentially outside 0-24 hours interval)
     *
     * @param baseDate is base date against which time is calculated
     * @return time relative to specified base date
     */
    public DtTimeS getTime(DtDate baseDate) {
        if (isPriv() || baseDate.isPriv()) {
            return DtTimeS.PRIV;
        }
        if (isME() || baseDate.isME()) {
            return DtTimeS.ME;
        }
        if ((isMax() && baseDate.isMax()) || (isMin() && baseDate.isMin())) {
            return DtTimeS.ofSeconds(0);
        }
        if (isMax() || baseDate.isMin()) {
            return DtTimeS.MAX;
        }
        if (isMin() || baseDate.isMax()) {
            return DtTimeS.MIN;
        }
        if (baseDate.equals(date)) {
            return time;
        }
        return DtTimeS.ofSeconds((int) Math.round(minus(DtDateTime.ofDate(baseDate))*86400d));
    }
    /**
     * Indicates if given value is regular datetime value. Regular values are valid values in period MIN ... MAX
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
     * @return {@code LocalDateTime} represented by this object
     */
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(date.getLocalDate(), time.getLocalTime());
    }

    /**
     * @return year from this date value
     */
    public int getYear() {
        if (isPriv()) {
            return DtInteger.PRIV;
        }
        if (isME()) {
            return DtInteger.ME;
        }
        return date.getYear();
    }

    /**
     * @return month from this date value
     */
    public int getMonthValue() {
        if (isPriv()) {
            return DtInteger.PRIV;
        }
        if (isME()) {
            return DtInteger.ME;
        }
        return date.getMonthValue();
    }

    /**
     * @return day of month from this date value
     */
    public int getDayOfMonth() {
        if (isPriv()) {
            return DtInteger.PRIV;
        }
        if (isME()) {
            return DtInteger.ME;
        }
        return date.getDayOfMonth();
    }

    /**
     * Returns a copy of this {@code DtDateTime} with the specified number of days added.
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
    public DtDateTime plusDays(int daysToAdd) {
        if (isPriv() || DtInteger.PRIV.equals(daysToAdd)) {
            return DtDateTime.PRIV;
        }
        if (isME() || DtInteger.ME.equals(daysToAdd)) {
            return DtDateTime.ME;
        }
        if (isMin() || isMax()) {
            return this;
        }
        if (DtInteger.MIN.equals(daysToAdd)) {
            return DtDateTime.MIN;
        }
        if (DtInteger.MAX.equals(daysToAdd)) {
            return DtDateTime.MAX;
        }
        if (daysToAdd == 0) {
            return this;
        }
        return ofDateTime(date.plusDays(daysToAdd), time);
    }

    /**
     * Returns a copy of this {@code DtDateTime} with the specified number of days added; version with fractional number
     * of days supported. This method adds the specified amount of days and ensures result remains valid. The result is
     * only invalid if the maximum/minimum year is exceeded. When function is applied on special value (MIN, MAX, PRIV,
     * ME), it is returned unchanged. If supplied integer value corresponds to one of special values (PRIV, ME),
     * corresponding special value is returned
     *
     * @param daysToAdd  the days to add, may be negative
     * @return a {@code DtDate} based on this date with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    @Nonnull
    public DtDateTime plusDays(double daysToAdd) {
        if (isPriv() || DtDouble.PRIV.equals(daysToAdd)) {
            return DtDateTime.PRIV;
        }
        if (isME() || DtDouble.ME.equals(daysToAdd)) {
            return DtDateTime.ME;
        }
        if (isMin() || isMax()) {
            return DtDateTime.this;
        }
        if (DtDouble.MIN.equals(daysToAdd)) {
            return DtDateTime.MIN;
        }
        if (DtDouble.MAX.equals(daysToAdd)) {
            return DtDateTime.MAX;
        }
        if (daysToAdd == 0) {
            return this;
        }
        int wholeDays = (int) Math.round(daysToAdd);
        return ofDateTime(date.plusDays(wholeDays), time.plusDays(daysToAdd - (double) wholeDays));
    }

    /**
     * Difference of dates (in days). Note that in case this or operand are special values, you will not get original
     * value when applying result of minus on operand
     *
     * @param date date to be subtracted
     * @return different of supplied dates in days. Returns PRIV if any of operands if missing privileges value, ME if
     * any of the operands is ME and not PRIV
     */
    public double minus(DtDateTime date) {
        if (isPriv() || date.isPriv()) {
            return DtDouble.PRIV;
        }
        if (isME() || date.isME()) {
            return DtDouble.ME;
        }
        if (isMax()) {
            return date.isMax() ? 0 : DtDouble.MAX;
        }
        if (isMin()) {
            return date.isMin() ? 0 : DtDouble.MIN;
        }
        if (date.isMax()) {
            return DtDouble.MIN;
        }
        if (date.isMin()) {
            return DtDouble.MAX;
        }
        return ((double) Duration.between(date.getLocalDateTime(), this.getLocalDateTime()).toSeconds()) / 86400;
    }

    /**
     * Converts {@code DtDate} value to Provys string representation (format DD.MM.YYYY HH:MI:SS)
     */
    @Nonnull
    public String toProvysValue() {
        return date.toProvysValue() + " " + time.toProvysValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DtDateTime that = (DtDateTime) o;

        if (!getDate().equals(that.getDate())) return false;
        return getTime().equals(that.getTime());
    }

    @Override
    public int hashCode() {
        int result = getDate().hashCode();
        result = 31 * result + getTime().hashCode();
        return result;
    }

    @Override
    public int compareTo(DtDateTime o) {
        var result = date.compareTo(o.date);
        if (result == 0) {
            return time.compareTo(o.time);
        }
        return result;
    }

    @Override
    public String toString() {
        if (equals(DtDateTime.PRIV)) {
            return PRIV_TEXT;
        }
        if (equals(DtDateTime.ME)) {
            return ME_TEXT;
        }
        if (equals(DtDateTime.MIN)) {
            return MIN_TEXT;
        }
        if (equals(DtDateTime.MAX)) {
            return MAX_TEXT;
        }
        return date.toString() + "T" + time.toString();
    }
}
