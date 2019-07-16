package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtIntegerTest {

    @Nonnull
    static Stream<Object[]> isValidTest() {
        return Stream.of(
                new Object[]{15482, true}
                , new Object[]{-87549645, true}
                , new Object[]{DtInteger.PRIV, true}
                , new Object[]{DtInteger.ME, true}
                , new Object[]{DtInteger.MIN, true}
                , new Object[]{DtInteger.MAX, true}
                , new Object[]{Integer.MIN_VALUE, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidTest(int value, boolean result) {
        assertThat(DtInteger.isValid(value)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isRegularTest() {
        return Stream.of(
                new Object[]{15482, true}
                , new Object[]{-87549645, true}
                , new Object[]{DtInteger.PRIV, false}
                , new Object[]{DtInteger.ME, false}
                , new Object[]{DtInteger.MIN, false}
                , new Object[]{DtInteger.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isRegularTest(int value, boolean result) {
        assertThat(DtInteger.isRegular(value)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isValidValueTest() {
        return Stream.of(
                new Object[]{15482, true}
                , new Object[]{-87549645, true}
                , new Object[]{DtInteger.PRIV, false}
                , new Object[]{DtInteger.ME, false}
                , new Object[]{DtInteger.MIN, true}
                , new Object[]{DtInteger.MAX, true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidValueTest(int value, boolean result) {
        assertThat(DtInteger.isValidValue(value)).isEqualTo(result);
    }
}