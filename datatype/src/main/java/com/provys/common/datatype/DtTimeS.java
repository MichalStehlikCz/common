package com.provys.common.datatype;

import javax.annotation.Nonnull;
import java.time.DateTimeException;
import java.time.LocalTime;

/**
 * Support for Provys domain TIME with subdomain S (time in seconds)
 */
public class DtTimeS {

    /**
     * Date value, returned when user doesn't have the rights to access the value
     */
    public static final DtTimeS PRIV = new DtTimeS(DtInteger.PRIV);

    /**
     * Date value, returned as indication of multi-value
     */
    public static final DtTimeS ME = new DtTimeS(DtInteger.ME);

    /**
     * Minimal date value, valid in Provys
     */
    public static final DtTimeS MIN = new DtTimeS(DtInteger.MIN);

    /**
     * Maximal date value, valid in Provys
     */
    public static final DtTimeS MAX = new DtTimeS(DtInteger.MAX);

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
        return of(time.getHour()*3600 + time.getMinute()*60 + time.getSecond() + (time.getNano() >= 500000000 ? 1 : 0));
    }

    /**
     * Return time object representing given time value (in seconds)
     *
     * @param time is time value to be represented
     * @return resulting value
     */
    @Nonnull
    public static DtTimeS of(int time) {
        if (DtInteger.isRegular(time)) {
            if ((time % 3600 == 0) && (time / 3600 < HOURS.length)) {
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
     * Default constructor. It is private, static functions should be used instead of constructor to retrieve instances
     * of localtime (to support caching)
     *
     * @param time is time in seconds new object should represent
     */
    private DtTimeS(int time) {
        this.time = time;
    }
}
