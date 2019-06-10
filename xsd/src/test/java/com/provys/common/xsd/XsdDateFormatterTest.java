package com.provys.common.xsd;

import javax.annotation.Nonnull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class XsdDateFormatterTest {

    @Nonnull
    static Stream<Object[]> getStrictPatternTest() {
        return Stream.of(
                new Object[]{"2018-03-14", true}
                , new Object[]{"2015-12-31", true}
                , new Object[]{"2018", false}
                , new Object[]{"2018-01", false}
                , new Object[]{"2018-01-24T12:00:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictPatternTest(String value, boolean match) {
        assertThat(XsdDateFormatter.STRICT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }

}