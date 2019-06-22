package com.provys.common.datatype;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtDateTest {

    @Test
    void ofLocalDateTest() {
        var value = LocalDate.of(1989, 11, 5);
        assertThat(DtDate.ofLocalDate(value).getLocalDate()).isEqualTo(value);
    }

    @Test
    void ofLocalDateNullTest() {
        //noinspection ConstantConditions
        assertThatThrownBy(() -> DtDate.ofLocalDate(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void ofTest() {
        int year = 2014;
        short month = 5;
        short day = 30;
        assertThat(DtDate.of(year, month, day).getLocalDate()).isEqualTo(LocalDate.of(year, month, day));
    }

    @Test
    void ofTest2() {
        int year = 2019;
        int month = 1;
        int day = 31;
        assertThat(DtDate.of(year, month, day).getLocalDate()).isEqualTo(LocalDate.of(year, month, day));
    }

    @Test
    void ofInstant() {
        Instant instant = Instant.parse("2019-01-12T10:51:13Z");
        ZoneId zone = ZoneId.of("GMT");
        assertThat(DtDate.ofInstant(instant, zone)).isEqualTo(DtDate.of(2019, 1, 12));
    }

    @Test
    void ofInstant2() {
        Instant instant = Instant.parse("2015-12-07T10:51:13Z");
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        assertThat(DtDate.ofInstant(instant)).isEqualTo(DtDate.of(dateTime.getYear(), dateTime.getMonthValue(),
                dateTime.getDayOfMonth()));
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"1989-11-25", LocalDate.of(1989, 11, 25)}
                , new Object[]{"1989-1-1", null}
                , new Object[]{"1989-11-25Z", null}
                , new Object[]{null, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String date, @Nullable LocalDate result) {
        if (result != null) {
            assertThat(DtDate.parse(date)).isEqualTo(DtDate.ofLocalDate(result));
        } else {
            assertThatThrownBy(() -> DtDate.parse(date));
        }
    }

    @Nonnull
    static Stream<Object[]> ofProvysValueTest() {
        return Stream.of(
                new Object[]{"25.11.1989", LocalDate.of(1989, 11, 25)}
                , new Object[]{"25.11.1989 00:00:00", LocalDate.of(1989, 11, 25)}
                , new Object[]{"1989-01-01", null}
                , new Object[]{"25.1.2018", null}
                , new Object[]{"2.01.2018", null}
                , new Object[]{"02.01.2018 05:00:00", null}
                , new Object[]{"02.01.2018 00:00", null}
                , new Object[]{null, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofProvysValueTest(String date, @Nullable LocalDate result) {
        if (result != null) {
            assertThat(DtDate.ofProvysValue(date)).isEqualTo(DtDate.ofLocalDate(result));
        } else {
            assertThatThrownBy(() -> DtDate.ofProvysValue(date));
        }
    }

    @Test
    void getYearTest() {
        assertThat(DtDate.of(1987, 12, 5).getYear()).isEqualTo(1987);
    }

    @Test
    void getMonthValueTest() {
        assertThat(DtDate.of(1987, 12, 5).getMonthValue()).isEqualTo(12);
    }

    @Test
    void getDayOfMonthTest() {
        assertThat(DtDate.of(1987, 12, 5).getDayOfMonth()).isEqualTo(5);
    }

    @Nonnull
    static Stream<Object[]> isRegularTest() {
        return Stream.of(
                new Object[]{DtDate.of(1989, 11, 25), true}
                , new Object[]{DtDate.MIN, true}
                , new Object[]{DtDate.MAX, true}
                , new Object[]{DtDate.PRIV, false}
                , new Object[]{DtDate.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isRegularTest(DtDate date, boolean result) {
        assertThat(date.isRegular()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> plusDaysTest() {
        return Stream.of(
                new Object[]{DtDate.of(1989, 11, 25), 10,
                        DtDate.of(1989, 12, 5)}
                , new Object[]{DtDate.of(1989, 11, 25), -10,
                        DtDate.of(1989, 11, 15)}
                , new Object[]{DtDate.of(2011, 12, 31), 1,
                        DtDate.of(2012, 1, 1)}
                , new Object[]{DtDate.of(2011, 1, 1), -1,
                        DtDate.of(2010, 12, 31)}
                , new Object[]{DtDate.PRIV, -1, DtDate.PRIV}
                , new Object[]{DtDate.ME, -1, DtDate.ME}
                , new Object[]{DtDate.MIN, -1, DtDate.MIN}
                , new Object[]{DtDate.MAX, -1, DtDate.MAX}
        );
    }

    @ParameterizedTest
    @MethodSource
    void plusDaysTest(DtDate date, int days, @Nullable DtDate result) {
        if (result != null) {
            assertThat(date.plusDays(days)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> date.plusDays(days)).isInstanceOf(DateTimeException.class);
        }
    }

    @Nonnull
    static Stream<Object[]> toProvysValueTest() {
        return Stream.of(
                new Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
                        "25.11.1989"}
                , new Object[]{DtDate.of(2012, (short) 11, (short) 30), "30.11.2012"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toProvysValueTest(DtDate date, String result) {
        assertThat(date.toProvysValue()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> equalsTest() {
        return Stream.of(
                new Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
                        DtDate.of(1989, (short) 11, (short) 25), true}
                , new Object[]{DtDate.of(1989, (short) 11, (short) 25), null, false}
                , new Object[]{DtDate.of(1989, (short) 11, (short) 25), "1989-11-25", false}
                , new Object[]{DtDate.of(1989, (short) 11, (short) 25),
                        DtDate.of(1989, (short) 11, (short) 24), false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void equalsTest(DtDate date, @Nullable Object other, boolean equal) {
        assertThat(date.equals(other)).isEqualTo(equal);
    }

    @Test
    void hashCodeTest() {
        assertThat(DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)).hashCode()).
                isEqualTo(DtDate.of(1989, (short) 11, (short) 25).hashCode());
    }

    @Nonnull
    static Stream<Object[]> compareToTest() {
        return Stream.of(
                new Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
                        DtDate.of(1989, (short) 11, (short) 25), 0}
                , new Object[]{DtDate.of(1989, (short) 11, (short) 25),
                        DtDate.of(1989, (short) 11, (short) 24), 1}
                , new Object[]{DtDate.of(1989, (short) 10, (short) 25),
                        DtDate.of(1989, (short) 11, (short) 24), -1}
        );
    }

    @ParameterizedTest
    @MethodSource
    void compareToTest(DtDate date, DtDate other, int result) {
        assertThat(date.compareTo(other)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
                        "1989-11-25"}
                , new Object[]{DtDate.of(2012, (short) 11, (short) 30), "2012-11-30"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(DtDate date, String result) {
        assertThat(date.toString()).isEqualTo(result);
    }
}