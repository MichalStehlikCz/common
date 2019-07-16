package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtDoubleTest {

    @Nonnull
    static Stream<Object[]> isValidTest() {
        return Stream.of(
                new Object[]{15482d, true}
                , new Object[]{-87549645d, true}
                , new Object[]{DtDouble.PRIV, true}
                , new Object[]{DtDouble.ME, true}
                , new Object[]{DtDouble.MIN, true}
                , new Object[]{DtDouble.MAX, true}
                , new Object[]{12548965486212d, false}
                , new Object[]{-2548965486212d, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidTest(double value, boolean result) {
        assertThat(DtDouble.isValid(value)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isRegularTest() {
        return Stream.of(
                new Object[]{15482d, true}
                , new Object[]{-87549645d, true}
                , new Object[]{DtDouble.PRIV, false}
                , new Object[]{DtDouble.ME, false}
                , new Object[]{DtDouble.MIN, false}
                , new Object[]{DtDouble.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isRegularTest(double value, boolean result) {
        assertThat(DtDouble.isRegular(value)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isValidValueTest() {
        return Stream.of(
                new Object[]{15482d, true}
                , new Object[]{-87549645d, true}
                , new Object[]{DtDouble.PRIV, false}
                , new Object[]{DtDouble.ME, false}
                , new Object[]{DtDouble.MIN, true}
                , new Object[]{DtDouble.MAX, true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValidValueTest(double value, boolean result) {
        assertThat(DtDouble.isValidValue(value)).isEqualTo(result);
    }
}