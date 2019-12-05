package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
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

    static Stream<Object[]> isPrivTest() {
        return Stream.of(
                new Object[]{15482d, false}
                , new Object[]{-87549645d, false}
                , new Object[]{DtDouble.PRIV, true}
                , new Object[]{DtDouble.ME, false}
                , new Object[]{DtDouble.MIN, false}
                , new Object[]{DtDouble.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isPrivTest(double value, boolean result) {
        assertThat(DtDouble.isPriv(value)).isEqualTo(result);
    }

    static Stream<Object[]> isMETest() {
        return Stream.of(
                new Object[]{15482d, false}
                , new Object[]{-87549645d, false}
                , new Object[]{DtDouble.PRIV, false}
                , new Object[]{DtDouble.ME, true}
                , new Object[]{DtDouble.MIN, false}
                , new Object[]{DtDouble.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMETest(double value, boolean result) {
        assertThat(DtDouble.isME(value)).isEqualTo(result);
    }

    static Stream<Object[]> isMinTest() {
        return Stream.of(
                new Object[]{15482d, false}
                , new Object[]{-87549645d, false}
                , new Object[]{DtDouble.PRIV, false}
                , new Object[]{DtDouble.ME, false}
                , new Object[]{DtDouble.MIN, true}
                , new Object[]{DtDouble.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMinTest(double value, boolean result) {
        assertThat(DtDouble.isMin(value)).isEqualTo(result);
    }

    @Nonnull
    static Stream<Object[]> isMaxTest() {
        return Stream.of(
                new Object[]{15482d, false}
                , new Object[]{-87549645d, false}
                , new Object[]{DtDouble.PRIV, false}
                , new Object[]{DtDouble.ME, false}
                , new Object[]{DtDouble.MIN, false}
                , new Object[]{DtDouble.MAX, true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMaxTest(double value, boolean result) {
        assertThat(DtDouble.isMax(value)).isEqualTo(result);
    }
}