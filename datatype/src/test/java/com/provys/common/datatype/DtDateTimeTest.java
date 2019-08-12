package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.DateTimeException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtDateTimeTest {

    @Nonnull
    static Stream<Object[]> ofDateTest() {
        return Stream.of(
                new Object[]{DtDate.of(1989, 11, 25)}
                , new Object[]{DtDate.MIN}
                , new Object[]{DtDate.MAX}
                , new Object[]{DtDate.PRIV}
                , new Object[]{DtDate.ME}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofDateTest(DtDate date) {
        var value = DtDateTime.ofDate(date);
        assertThat(value.getDate()).isEqualTo(date);
        if (date.isRegular()) {
            assertThat(value.getTime().getSeconds()).isEqualTo(0);
        }
    }

    @Nonnull
    static Stream<Object[]> ofDateTimeTest() {
        return Stream.of(
                new Object[]{DtDate.of(1989, 11, 25), DtTimeS.ofSeconds(58421),
                        null, DtDate.of(1989, 11, 25), DtTimeS.ofSeconds(58421)}
                , new Object[]{DtDate.of(2005, 7, 15), DtTimeS.ofDayToMinute(3, 11, 54),
                        null, DtDate.of(2005, 7, 18), DtTimeS.ofHourToMinute(11, 54)}
                , new Object[]{DtDate.MIN, DtTimeS.ofSeconds(150), null, DtDate.MIN, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDate.MIN, DtTimeS.MIN, null, DtDate.MIN, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDate.MIN, DtTimeS.MAX, "Cannot create datetime from min date and max time", null, null}
                , new Object[]{DtDate.MAX, DtTimeS.ofSeconds(150), null, DtDate.MAX, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDate.MAX, DtTimeS.MAX, null, DtDate.MAX, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDate.MAX, DtTimeS.MIN, "Cannot create datetime from max date and min time", null, null}
                , new Object[]{DtDate.PRIV, DtTimeS.ME, null, DtDate.PRIV, DtTimeS.PRIV}
                , new Object[]{DtDate.of(1989, 11, 25), DtTimeS.PRIV, null, DtDate.PRIV, DtTimeS.PRIV}
                , new Object[]{DtDate.ME, DtTimeS.MIN, null, DtDate.ME, DtTimeS.ME}
                , new Object[]{DtDate.MAX, DtTimeS.ME, null, DtDate.ME, DtTimeS.ME}
        );
    }

    @ParameterizedTest
    @MethodSource
    void ofDateTimeTest(DtDate date, DtTimeS time, @Nullable String message, @Nullable DtDate resultDate,
                        @Nullable DtTimeS resultTime) {
        if (message == null) {
            var value = DtDateTime.ofDateTime(date, time);
            assertThat(value.getDate()).isEqualTo(resultDate);
            assertThat(value.getTime()).isEqualTo(resultTime);
        } else {
            assertThatThrownBy(() -> DtDateTime.ofDateTime(date, time)).hasMessage(message);
        }
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"2005-07-15T03:11:54",
                        null, DtDate.of(2005, 7, 15),
                        DtTimeS.ofHourToSecond(3, 11, 54)}
                , new Object[]{DtDateTime.MIN_TEXT, null, DtDate.MIN, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDateTime.MAX_TEXT, null, DtDate.MAX, DtTimeS.ofSeconds(0)}
                , new Object[]{DtDateTime.PRIV_TEXT, null, DtDate.PRIV, DtTimeS.PRIV}
                , new Object[]{DtDateTime.ME_TEXT, null, DtDate.ME, DtTimeS.ME}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String text, @Nullable String message, @Nullable DtDate resultDate,
                        @Nullable DtTimeS resultTime) {
        if (message == null) {
            var value = DtDateTime.parse(text);
            assertThat(value.getDate()).isEqualTo(resultDate);
            assertThat(value.getTime()).isEqualTo(resultTime);
        } else {
            assertThatThrownBy(() -> DtDateTime.parse(text)).hasMessage(message);
        }
    }
}