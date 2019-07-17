package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtTimeSTest {

    @Nonnull
    static Stream<Object[]> ofLocalTimeTest() {
        return Stream.of(
                new Object[]{LocalTime.of(12, 15, 24),
                        DtTimeS.ofHourToSecond(12, 15, 24)}
                , new Object[]{LocalTime.of(3, 27),
                        DtTimeS.ofHourToMinute(3, 27)}
                , new Object[]{LocalTime.of(1, 15, 4, 499999999),
                        DtTimeS.ofHourToSecond(1, 15, 4)}
                , new Object[]{LocalTime.of(1, 15, 4, 500000000),
                        DtTimeS.ofHourToSecond(1, 15, 5)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofLocalTimeTest(LocalTime time, DtTimeS result) {
        assertThat(DtTimeS.ofLocalTime(time)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"12:15:24", DtTimeS.ofHourToSecond(12, 15, 24)}
                , new Object[]{"3:27", DtTimeS.ofHourToMinute(3, 27)}
                , new Object[]{"-1:15:4:00", DtTimeS.ofHourToSecond(true, 1, 15, 4)}
                , new Object[]{"124:7:56:00", DtTimeS.ofHourToSecond(124, 7, 56)}
                , new Object[]{"10:74:00:00", null}
                , new Object[]{"10:00:60:00", null}
                , new Object[]{"10:00:00:05", null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String value, @Nullable DtTimeS result) {
        if (result != null) {
            assertThat(DtTimeS.parse(value)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> DtTimeS.parse(value)).isInstanceOf(DateTimeParseException.class);
        }
    }

    @Nonnull
    static Stream<Object[]> isRegularTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), true}
                , new Object[]{DtTimeS.MIN, false}
                , new Object[]{DtTimeS.MAX, false}
                , new Object[]{DtTimeS.PRIV, false}
                , new Object[]{DtTimeS.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isRegularTest(DtTimeS value, boolean result) {
        assertThat(value.isRegular()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isValidValueTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), true}
                , new Object[]{DtTimeS.MIN, true}
                , new Object[]{DtTimeS.MAX, true}
                , new Object[]{DtTimeS.PRIV, false}
                , new Object[]{DtTimeS.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidValueTest(DtTimeS value, boolean result) {
        assertThat(value.isValidValue()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isPrivTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), false}
                , new Object[]{DtTimeS.MIN, false}
                , new Object[]{DtTimeS.MAX, false}
                , new Object[]{DtTimeS.PRIV, true}
                , new Object[]{DtTimeS.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isPrivTest(DtTimeS value, boolean result) {
        assertThat(value.isPriv()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isMETest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), false}
                , new Object[]{DtTimeS.MIN, false}
                , new Object[]{DtTimeS.MAX, false}
                , new Object[]{DtTimeS.PRIV, false}
                , new Object[]{DtTimeS.ME, true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMETest(DtTimeS value, boolean result) {
        assertThat(value.isME()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isMinTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), false}
                , new Object[]{DtTimeS.MIN, true}
                , new Object[]{DtTimeS.MAX, false}
                , new Object[]{DtTimeS.PRIV, false}
                , new Object[]{DtTimeS.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMinTest(DtTimeS value, boolean result) {
        assertThat(value.isMin()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isMaxTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToMinute(12, 25), false}
                , new Object[]{DtTimeS.MIN, false}
                , new Object[]{DtTimeS.MAX, true}
                , new Object[]{DtTimeS.PRIV, false}
                , new Object[]{DtTimeS.ME, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMaxTest(DtTimeS value, boolean result) {
        assertThat(value.isMax()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> plusDaysTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 12.4/86400d,
                        DtTimeS.ofHourToSecond(12, 15, 36)}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 2d + 15d/86400,
                        DtTimeS.ofDayToSecond(2, 3, 27, 15)}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), -1d/24d,
                        DtTimeS.ofHourToMinute(true, 1, 0)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        1d / 24d + 15d / 24d / 60d + 4d / 86400d, DtTimeS.ofSeconds(0)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), -1d,
                        DtTimeS.ofHourToSecond(true, 48, 0, 0)}
                , new Object[]{DtTimeS.PRIV, -1d, DtTimeS.PRIV}
                , new Object[]{DtTimeS.ME, -1d, DtTimeS.ME}
                , new Object[]{DtTimeS.MIN, -1d, DtTimeS.MIN}
                , new Object[]{DtTimeS.MAX, -1d, DtTimeS.MAX}
                , new Object[]{DtTimeS.ofHourToMinute(24, 0), DtDouble.PRIV, DtTimeS.PRIV}
                , new Object[]{DtTimeS.ofHourToMinute(24, 0), DtDouble.ME, DtTimeS.ME}
                , new Object[]{DtTimeS.ofHourToMinute(24, 0), DtDouble.MIN, DtTimeS.MIN}
                , new Object[]{DtTimeS.ofHourToMinute(24, 0), DtDouble.MAX, DtTimeS.MAX}
        );
    }

    @ParameterizedTest
    @MethodSource
    void plusDaysTest(DtTimeS value, Double plusDays, DtTimeS result) {
        assertThat(value.plusDays(plusDays)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getDaysTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 0}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 0}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), -1}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), -1}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 5}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), -6}
                , new Object[]{DtTimeS.PRIV, DtInteger.PRIV}
                , new Object[]{DtTimeS.ME, DtInteger.ME}
                , new Object[]{DtTimeS.MIN, DtInteger.MIN}
                , new Object[]{DtTimeS.MAX, DtInteger.MAX}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getDaysTest(DtTimeS value, Integer result) {
        assertThat(value.getDays()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toDaysTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24),
                        (double) (3600 * 12 + 60 * 15 + 24) / 86400d}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), (double) (3600 * 3 + 60 * 27) / 86400d}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        (double) (-3600 - 60 * 15 - 4) / 86400d}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56),
                        (double) (3600 * 124 + 60 * 7 + 56) / 86400d}
                , new Object[]{DtTimeS.PRIV, DtDouble.PRIV}
                , new Object[]{DtTimeS.ME, DtDouble.ME}
                , new Object[]{DtTimeS.MIN, DtDouble.MIN}
                , new Object[]{DtTimeS.MAX, DtDouble.MAX}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toDaysTest(DtTimeS value, Double result) {
        assertThat(value.toDays()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getTime24Test() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24),
                        DtTimeS.ofHourToSecond(12, 15, 24)}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27),
                        DtTimeS.ofHourToMinute(3, 27)}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), DtTimeS.ofHourToMinute(0, 0)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        DtTimeS.ofHourToSecond(22, 44, 56)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0),
                        DtTimeS.ofHourToMinute(0, 0)}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56),
                        DtTimeS.ofHourToSecond(4, 7, 56)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56),
                        DtTimeS.ofHourToSecond(19, 52, 4)}
                , new Object[]{DtTimeS.PRIV, DtTimeS.PRIV}
                , new Object[]{DtTimeS.ME, DtTimeS.ME}
                , new Object[]{DtTimeS.MIN, DtTimeS.ofSeconds(0)}
                , new Object[]{DtTimeS.MAX, DtTimeS.ofSeconds(0)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getTime24Test(DtTimeS value, DtTimeS result) {
        assertThat(value.getTime24()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getHoursTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 12}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 3}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), -1}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), -24}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 124}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), -124}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getHoursTest(DtTimeS value, Integer result) {
        assertThat(value.getHours()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getHours24Test() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 12}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 3}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), 22}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 4}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), 19}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getHours24Test(DtTimeS value, Integer result) {
        assertThat(value.getHours24()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toHoursTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24),
                        (double) (3600 * 12 + 60 * 15 + 24) / 3600d}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), (double) (3600 * 3 + 60 * 27) / 3600d}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        (double) (-3600 - 60 * 15 - 4) / 3600d}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56),
                        (double) (3600 * 124 + 60 * 7 + 56) / 3600d}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toHoursTest(DtTimeS value, Double result) {
        assertThat(value.toHours()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getMinutesTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 15}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 27}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), -15}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 7}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), -7}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getMinutesTest(DtTimeS value, Integer result) {
        assertThat(value.getMinutes()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getMinutes24Test() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 15}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 27}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), 44}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 7}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), 52}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getMinutes24Test(DtTimeS value, Integer result) {
        assertThat(value.getMinutes24()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toMinutesTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24),
                        (double) (3600 * 12 + 60 * 15 + 24) / 60d}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), (double) (3600 * 3 + 60 * 27) / 60d}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        (double) (-3600 - 60 * 15 - 4) / 60d}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56),
                        (double) (3600 * 124 + 60 * 7 + 56) / 60d}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toMinutesTest(DtTimeS value, Double result) {
        assertThat(value.toMinutes()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getSecondsTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 24}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 0}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), -4}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 56}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), -56}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getSecondsTest(DtTimeS value, Integer result) {
        assertThat(value.getSeconds()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> getSeconds24Test() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), 24}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), 0}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), 56}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), 0}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), 56}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), 4}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getSeconds24Test(DtTimeS value, Integer result) {
        assertThat(value.getSeconds24()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toSecondsTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24),
                        (double) (3600 * 12 + 60 * 15 + 24)}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), (double) (3600 * 3 + 60 * 27)}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4),
                        (double) (-3600 - 60 * 15 - 4)}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56),
                        (double) (3600 * 124 + 60 * 7 + 56)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toSecondsTest(DtTimeS value, Double result) {
        assertThat(value.toSeconds()).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 15, 24), "12:15:24"}
                , new Object[]{DtTimeS.ofHourToMinute(3, 27), "03:27:00"}
                , new Object[]{DtTimeS.ofHourToMinute(0, 0), "00:00:00"}
                , new Object[]{DtTimeS.ofHourToSecond(true, 1, 15, 4), "-01:15:04"}
                , new Object[]{DtTimeS.ofHourToSecond(true, 24, 0, 0), "-24:00:00"}
                , new Object[]{DtTimeS.ofHourToSecond(124, 7, 56), "124:07:56"}
                , new Object[]{DtTimeS.ofHourToSecond(true, 124, 7, 56), "-124:07:56"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(DtTimeS value, String result) {
        assertThat(value.toString()).isEqualTo(result);
    }
}