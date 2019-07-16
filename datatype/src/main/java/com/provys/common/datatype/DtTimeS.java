package com.provys.common.datatype;

import javax.annotation.Nonnull;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Support for Provys domain TIME with subdomain S (time in seconds)
 */
@SuppressWarnings("WeakerAccess")
public class DtTimeS implements Comparable<DtTimeS> {

    /**
     * Date value, returned when user doesn't have the rights to access the value
     */
    public static final DtTimeS PRIV = new DtTimeS(DtInteger.PRIV, false);

    /**
     * Date value, returned as indication of multi-value
     */
    public static final DtTimeS ME = new DtTimeS(DtInteger.ME, false);

    /**
     * Minimal date value, valid in Provys
     */
    public static final DtTimeS MIN = new DtTimeS(DtInteger.MIN, false);

    /**
     * Maximal date value, valid in Provys
     */
    public static final DtTimeS MAX = new DtTimeS(DtInteger.MAX, false);

    /**
     * whole hours are used pretty often, so it might be good idea to cache them...
     */
    private static final DtTimeS[] HOURS = new DtTimeS[31];
    static {
        for (int i = 0; i < 31; i++) {
            HOURS[i] = new DtTimeS(i * 3600);
        }
    }

    /**
     * Return time object representing given {@code LocalTime} value.
     *
     * @param time is time value new object should represent
     * @return Provys time value corresponding to supplied {@code LocalTime} value. Fractional part is rounded to whole
     * seconds.
     */
    @Nonnull
    public static DtTimeS ofLocalTime(LocalTime time) {
        return ofHourToNano(time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
    }

    /**
     * Return time object representing given time value (in seconds)
     *
     * @param time is time value to be represented
     * @return resulting value
     */
    @Nonnull
    public static DtTimeS ofSeconds(int time) {
        if (DtInteger.isRegular(time)) {
            if ((time >= 0) && (time % 3600 == 0) && (time / 3600 < HOURS.length)) {
                return HOURS[time / 3600];
            }
            return new DtTimeS(time);
        }
        if (time == DtInteger.PRIV) {
            return PRIV;
        }
        if (time == DtInteger.ME) {
            return ME;
        }
        if (time == DtInteger.MIN) {
            return MIN;
        }
        if (time == DtInteger.MAX) {
            return MAX;
        }
        throw new DateTimeException(time + " is not valid time - must be regular integer value in interval " +
                DtInteger.MIN + " .. " + DtInteger.MAX);
    }

    private static void checkSecondToNano(int seconds, int nanoSeconds) {
        if (seconds < 0) {
            throw new DateTimeException("Negative number of seconds supplied; use negative sign instead");
        }
        if (nanoSeconds < 0) {
            throw new DateTimeException("Negative number of nano-seconds supplied; use negative sign instead");
        }
        if (nanoSeconds >= 1000000000) {
            throw new DateTimeException("Number of nanoseconds bigger than 1000000000 not allowed");
        }
    }

    private static void checkMinuteToNano(int minutes, int seconds, int nanoSeconds) {
        if (minutes < 0) {
            throw new DateTimeException("Negative number of minutes supplied; use negative sign instead");
        }
        if (seconds >= 60) {
            throw new DateTimeException("Number of seconds in minute is limited by 60");
        }
        checkSecondToNano(seconds, nanoSeconds);
    }

    private static void checkHourToNano(int hours, int minutes, int seconds, int nanoSeconds) {
        if (hours < 0) {
            throw new DateTimeException("Negative number of hours supplied; use negative sign instead");
        }
        if (minutes >= 60) {
            throw new DateTimeException("Number of minutes in hour is limited by 60");
        }
        checkMinuteToNano(minutes, seconds, nanoSeconds);
    }

    @Nonnull
    private static DtTimeS ofDayToNanoNoCheck(boolean negative, int days, int hours, int minutes, int seconds,
                                              int nanoSeconds) {
        int time = days * 86400 + hours*3600 + minutes*60 + seconds + (nanoSeconds >= 500000000 ? 1 : 0);
        if (negative) {
            time = -time;
        }
        return ofSeconds(time);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param days is number of days; might be positive or negative, in case of negative value, whole time value is
     *             negative
     * @param hours is number of hours, in range 0 .. 23
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     * @param nanoSeconds is number of nanoseconds in range 0 .. 999999999
     */
    @Nonnull
    public static DtTimeS ofDayToNano(int days, int hours, int minutes, int seconds, int nanoSeconds) {
        if (hours >= 24) {
            throw new DateTimeException("Number of hours in day is limited by 24");
        }
        checkHourToNano(hours, minutes, seconds, nanoSeconds);
        return ofDayToNanoNoCheck(false, days, hours, minutes, seconds, nanoSeconds);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param days is number of days; might be positive or negative, in case of negative value, whole time value is
     *             negative
     * @param hours is number of hours, in range 0 .. 23
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofDayToSecond(int days, int hours, int minutes, int seconds) {
        return ofDayToNano(days, hours, minutes, seconds, 0);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param days is number of days; might be positive or negative, in case of negative value, whole time value is
     *             negative
     * @param hours is number of hours, in range 0 .. 23
     * @param minutes is number of minutes, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofDayToMinute(int days, int hours, int minutes) {
        return ofDayToSecond(days, hours, minutes, 0);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param negative indicates if time is negative (bellow zero) or positive
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     * @param nanoSeconds is number of nanoseconds in range 0 .. 999999999
     */
    @Nonnull
    public static DtTimeS ofHourToNano(boolean negative, int hours, int minutes, int seconds, int nanoSeconds) {
        checkHourToNano(hours, minutes, seconds, nanoSeconds);
        return ofDayToNanoNoCheck(negative, 0, hours, minutes, seconds, nanoSeconds);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     * @param nanoSeconds is number of nanoseconds in range 0 .. 999999999
     */
    @Nonnull
    public static DtTimeS ofHourToNano(int hours, int minutes, int seconds, int nanoSeconds) {
        return ofHourToNano(false, hours, minutes, seconds, nanoSeconds);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param negative indicates if time is negative (bellow zero) or positive
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofHourToSecond(boolean negative, int hours, int minutes, int seconds) {
        return ofHourToNano(negative, hours, minutes, seconds, 0);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     * @param seconds is number of seconds, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofHourToSecond(int hours, int minutes, int seconds) {
        return ofHourToSecond(false, hours, minutes, seconds);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param negative indicates if time is negative (bellow zero) or positive
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofHourToMinute(boolean negative, int hours, int minutes) {
        return ofHourToSecond(negative, hours, minutes, 0);
    }

    /**
     * Return time object, representing given value (per parts).
     *
     * @param hours is number of hours, must be positive or zero
     * @param minutes is number of minutes, in range 0 .. 59
     */
    @Nonnull
    public static DtTimeS ofHourToMinute(int hours, int minutes) {
        return ofHourToMinute(false, hours, minutes);
    }

    /**
     * Parse value from text. Format is [-]HH:MM:SS; frame part can be specified, but must be 00. Hours might go beyond 24
     * hours.
     *
     * @param value is text to be parsed
     * @return valid {@code DtTimeS} value corresponding to supplied text
     */
    @Nonnull
    public static DtTimeS parse(String value) {
        var parser = new StringParser(value);
        var negative = false;
        if (parser.peek() == '-') {
            negative = true;
            parser.next();
        }
        var hours = parser.readUnsignedInt(1, 3);
        if (parser.next() != ':') {
            throw new DateTimeParseException("Invalid Provys time string format delimiter " + parser.current(), value,
                    parser.getPos());
        }
        var minutes = parser.readUnsignedInt(1, 2);
        if (!parser.hasNext()) {
            try {
                return ofHourToMinute(negative, hours, minutes);
            } catch (DateTimeException e) {
                throw new DateTimeParseException(e.getMessage(), value, parser.getPos());
            }
        }
        if (parser.next() != ':') {
            throw new DateTimeParseException("Invalid Provys time string format delimiter " + parser.current(), value,
                    parser.getPos());
        }
        var seconds = parser.readUnsignedInt(1, 2);
        if (parser.hasNext() && (parser.peek() == ':')) {
            parser.next();
            var nanoSeconds = parser.readUnsignedInt(1, 9);
            if (nanoSeconds != 0) {
                throw new DateTimeParseException(
                        "Invalid Provys time in seconds format; frame part expected to be zero", value, parser.getPos());
            }
        }
        try {
            return ofHourToSecond(negative, hours, minutes, seconds);
        } catch (DateTimeException e) {
            throw new DateTimeParseException(e.getMessage(), value, parser.getPos());
        }
    }

    /**
     * Retrieve instance of {@code DtTimeS} corresponding to current time (in default time-zone).
     */
    @Nonnull
    public static DtTimeS now() {
        return ofLocalTime(LocalTime.now());
    }

    /**
     * Time held is represented as time in seconds. DtInteger MIN and MAX values should be sufficient for time data,
     * held in Provys (as these are usually limited to to just slightly more than single day - longer time intervals
     * are expressed as duration in days (as it is natural time interval measurement in Provys)
     */
    private final int time;

    /**
     * Constructor supporting even special values. It is private, static functions should be used instead of constructor
     * to retrieve instances of localtime (to allow caching)
     *
     * @param time is time in seconds new object should represent
     * @param regular indicates if only regular time values are allowed
     */
    private DtTimeS(int time, boolean regular) {
        if (regular) {
            if (!DtInteger.isRegular(time)) {
                throw new DateTimeException("Only regular time values allowed in constructor, not " + time);
            }
        } else {
            if (!DtInteger.isValid(time)) {
                throw new DateTimeException("Only valid time values allowed in constructor, not " + time);
            }
        }
        this.time = time;
    }

    /**
     * Default constructor, only supporting regular values.
     *
     * @param time is time in seconds new object should represent
     */
    private DtTimeS(int time) {
        this(time, true);
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
     * Subtract given amount of days from time
     */
    public DtTimeS plusDays(double daysToAdd) {
        if (Math.abs(daysToAdd) * 86400 < 0.5) {
            return this;
        }
        return ofSeconds((int) Math.round(time + daysToAdd * 86400));
    }

    /**
     * Get number of days in this time item. Only whole days count when going to positive, even partial days count on
     * negative side.
     *
     * @return number of days this time represents
     */
    public int getDays() {
        if (time >= 0) {
            return time / 86400;
        } else {
            return (time - 86359) / 86400;
        }
    }

    /**
     * Get given time expressed in days as unit.
     *
     * @return number of days (fractional) represented by this time
     */
    public double toDays() {
        return ((double) time) / 86400d;
    }

    /**
     * Get time with days removed.
     *
     * @return time with whole days removed, clipped to 0-24 hours interval
     */
    public DtTimeS getTime24() {
        if (getDays() == 0) {
            return this;
        }
        return ofSeconds(time - 86400 * getDays());
    }

    /**
     * Get number of hours in this time item, minutes are trimmed, no 24 hout limit and negative values are treated as
     * negative values
     *
     * @return number of hours this time represents
     */
    public int getHours() {
        return time / 3600;
    }

    /**
     * Get number of hours in this time item, but remove days part first.
     *
     * @return number of hours this time represents in range 0-23
     */
    public int getHours24() {
        return (time - 86400 * getDays()) / 3600;
    }

    /**
     * Get amount of hours this time represents.
     *
     * @return hours represented by this time
     */
    public double toHours() {
        return (double) time / 3600d;
    }

    /**
     * Get number of minutes in this item. In case of negative time, non-positive value is returned
     *
     * @return minute part of this time
     */
    public int getMinutes() {
        return (time % 3600) / 60;
    }

    /**
     * Get number of minutes in this item. Days part is subtracted first, resulting in this function always returning
     * non-negative value
     *
     * @return minute part of this time
     */
    public int getMinutes24() {
        return ((time - 86400 * getDays()) % 3600) / 60;
    }

    /**
     * Convert given time to amount of minutes
     *
     * @return minute length this time represents
     */
    public double toMinutes() {
        return (double) time / 60d;
    }

    /**
     * Get number of seconds in this item. In case of negative time, non-positive value is returned
     *
     * @return second part of this time
     */
    public int getSeconds() {
        return time % 60;
    }

    /**
     * Get number of seconds in this item. Days part is subtracted first, resulting in this function always returning
     * non-negative value
     *
     * @return second part of this time
     */
    public int getSeconds24() {
        return (time - 86400 * getDays()) % 60;
    }

    /**
     * @return time in seconds
     */
    public double toSeconds() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DtTimeS dtTimeS = (DtTimeS) o;

        return time == dtTimeS.time;
    }

    @Override
    public int hashCode() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%s%02d:%02d:%02d", (time < 0) ? "-" : "", Math.abs(getHours()), Math.abs(getMinutes()),
                Math.abs(getSeconds()));
    }

    @Override
    public int compareTo(DtTimeS other) {
        return Double.compare(this.toSeconds(), other.toSeconds());
    }
}
