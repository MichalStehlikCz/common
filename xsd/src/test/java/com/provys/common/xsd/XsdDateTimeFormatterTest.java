package com.provys.common.xsd;

import javax.annotation.Nonnull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CellAddressIntTest {

    @Nonnull
    static Stream<Object[]> getStrictPatternTest() {
        return Stream.of(
                new Object[]{"2018-03-14T23:30:28.123456789Z", true}
                , new Object[]{"2018-03-14T23:30:00Z", true}
                , new Object[]{"2018-03-14T23:30:00z", false}
                , new Object[]{"2018-03-14T23:30:00+02:00", true}
                , new Object[]{"2018-03-14T23:30:00+0200", false}
                , new Object[]{"2018-03-14T23:30:00+02", false}
                , new Object[]{"2018-03-14T23:30:00", true}
                , new Object[]{"2018-03-14 23:30:00", false}
                , new Object[]{"2018-03-14t23:30:00", false}
                , new Object[]{"2018-03-14T23:30+02:00", false}
                , new Object[]{"2018-03-14", false}
                , new Object[]{"2018-03-14X23:30:00", false}
                , new Object[]{"2018", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictPatternTest(String value, boolean match) {
        assertThat(XsdDateTimeFormatter.STRICT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getLenientPatternTest() {
        return Stream.of(
                new Object[]{"2018-03-14T23:30:28.123456789Z", true}
                , new Object[]{"2018-03-14T23:30:00Z", true}
                , new Object[]{"2018-03-14T23:30:00z", true}
                , new Object[]{"2018-03-14T23:30:00+02:00", true}
                , new Object[]{"2018-03-14T23:30:00+0200", true}
                , new Object[]{"2018-03-14T23:30:00+02", true}
                , new Object[]{"2018-03-14T23:30:00", true}
                , new Object[]{"2018-03-14 23:30:00", true}
                , new Object[]{"2018-03-14t23:30:00", true}
                , new Object[]{"2018-03-14T23:30+02:00", true}
                , new Object[]{"2018-03-14", false}
                , new Object[]{"2018-03-14X23:30:00", false}
                , new Object[]{"2018", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientPatternTest(String value, boolean match) {
        assertThat(XsdDateTimeFormatter.LENIENT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }
}