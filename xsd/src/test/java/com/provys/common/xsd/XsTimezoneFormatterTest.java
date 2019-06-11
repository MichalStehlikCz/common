package com.provys.common.xsd;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class XsTimezoneFormatterTest {

    @Nonnull
    static Stream<Object[]> getStrictRegexTest() {
        return Stream.of(
                new Object[]{"+00:00", true}
                , new Object[]{"+01:00", true}
                , new Object[]{"-06:00", true}
                , new Object[]{"+05:30", true}
                , new Object[]{"Z", true}
                , new Object[]{"z", false}
                , new Object[]{"-07", false}
                , new Object[]{"+0500", false}
                , new Object[]{"01:00", false}
                , new Object[]{"UTC", false}
                , new Object[]{"(Prague)", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictRegexTest(String value, boolean match) {
        assertThat(Pattern.compile(XsTimezoneFormatter.STRICT_REGEX).matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getLenientRegexTest() {
        return Stream.of(
                new Object[]{"+00:00", true}
                , new Object[]{"+01:00", true}
                , new Object[]{"-06:00", true}
                , new Object[]{"+05:30", true}
                , new Object[]{"Z", true}
                , new Object[]{"z", true}
                , new Object[]{"-07", true}
                , new Object[]{"+0500", false}
                , new Object[]{"01:00", false}
                , new Object[]{"UTC", false}
                , new Object[]{"(Prague)", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientRegexTest(String value, boolean match) {
        assertThat(Pattern.compile(XsTimezoneFormatter.LENIENT_REGEX).matcher(value).matches()).isEqualTo(match);
    }
}