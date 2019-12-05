package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
class ZoneOffsetUtilTest {

    @Nonnull
    static Stream<Object[]> isValidIsoStrictTest() {
        return Stream.of(
                new Object[]{"Z", true}
                , new Object[]{"+10:00", true}
                , new Object[]{"-05:30", true}
                , new Object[]{"+00:00", true}
                , new Object[]{"+14:00", true}
                , new Object[]{"-14:00", true}
                , new Object[]{"+15:00", false}
                , new Object[]{"-16:00", false}
                , new Object[]{"+12", true}
                , new Object[]{"-05", true}
                , new Object[]{"x-05", false}
                , new Object[]{"-05x", false}
                , new Object[]{"z", false}
                , new Object[]{"+1000", false}
                , new Object[]{"-0900", false}
                , new Object[]{"+100000", false}
                , new Object[]{"+10:0", false}
                , new Object[]{"+5:00", false}
                , new Object[]{"+5", true}
                , new Object[]{"-05:30:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidIsoStrictTest(String text, boolean result) {
        assertThat(ZoneOffsetUtil.isValidIsoStrict(text)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isValidIsoLenientTest() {
        return Stream.of(
                new Object[]{"Z", true}
                , new Object[]{"+10:00", true}
                , new Object[]{"-05:30", true}
                , new Object[]{"+00:00", true}
                , new Object[]{"+14:00", true}
                , new Object[]{"-14:00", true}
                , new Object[]{"+15:00", true}
                , new Object[]{"-16:00", true}
                , new Object[]{"+19:00", false}
                , new Object[]{"-19:00", false}
                , new Object[]{"+12", true}
                , new Object[]{"-05", true}
                , new Object[]{"x-05", false}
                , new Object[]{"-05x", false}
                , new Object[]{"z", true}
                , new Object[]{"+1000", true}
                , new Object[]{"-0900", true}
                , new Object[]{"+100000", true}
                , new Object[]{"+10:0", false}
                , new Object[]{"+5:00", false}
                , new Object[]{"+5", true}
                , new Object[]{"-05:30:00", true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidIsoLenientTest(String text, boolean result) {
        assertThat(ZoneOffsetUtil.isValidIsoLenient(text)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> parseIsoTest() {
        return Stream.of(
                new Object[]{"Z", ZoneOffset.ofHours(0)}
                , new Object[]{"+10:00", ZoneOffset.ofHours(10)}
                , new Object[]{"-05:30", ZoneOffset.ofHoursMinutes(-5, -30)}
                , new Object[]{"+00:00", ZoneOffset.ofHours(0)}
                , new Object[]{"+14:00", ZoneOffset.ofHours(14)}
                , new Object[]{"-14:00", ZoneOffset.ofHours(-14)}
                , new Object[]{"+15:00", ZoneOffset.ofHours(15)}
                , new Object[]{"-16:00", ZoneOffset.ofHours(-16)}
                , new Object[]{"+12", ZoneOffset.ofHours(12)}
                , new Object[]{"-05", ZoneOffset.ofHours(-5)}
                , new Object[]{"x-05", null}
                , new Object[]{"-05x", null}
                , new Object[]{"z", ZoneOffset.ofHours(0)}
                , new Object[]{"+1000", ZoneOffset.ofHours(10)}
                , new Object[]{"-0900", ZoneOffset.ofHours(-9)}
                , new Object[]{"+10:0", null}
                , new Object[]{"+5:00", null}
                , new Object[]{"+5", ZoneOffset.ofHours(5)}
                , new Object[]{"-05:30:00", ZoneOffset.ofHoursMinutes(-5, -30)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseIsoTest(String text, @Nullable ZoneOffset result) {
        if (result == null) {
            assertThatThrownBy(() -> ZoneOffsetUtil.parseIso(text)).isInstanceOf(DateTimeParseException.class);
        } else {
            assertThat(ZoneOffsetUtil.parseIso(text)).isEqualTo(result);
        }
    }
}