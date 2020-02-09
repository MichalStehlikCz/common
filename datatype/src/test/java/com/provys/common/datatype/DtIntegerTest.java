package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
class DtIntegerTest {

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

    static Stream<Object[]> isPrivTest() {
        return Stream.of(
                new Object[]{15482, false}
                , new Object[]{-87549645, false}
                , new Object[]{DtInteger.PRIV, true}
                , new Object[]{DtInteger.ME, false}
                , new Object[]{DtInteger.MIN, false}
                , new Object[]{DtInteger.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isPrivTest(int value, boolean result) {
        assertThat(DtInteger.isPriv(value)).isEqualTo(result);
    }

    static Stream<Object[]> isMETest() {
        return Stream.of(
                new Object[]{15482, false}
                , new Object[]{-87549645, false}
                , new Object[]{DtInteger.PRIV, false}
                , new Object[]{DtInteger.ME, true}
                , new Object[]{DtInteger.MIN, false}
                , new Object[]{DtInteger.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMETest(int value, boolean result) {
        assertThat(DtInteger.isME(value)).isEqualTo(result);
    }

    static Stream<Object[]> isMinTest() {
        return Stream.of(
                new Object[]{15482, false}
                , new Object[]{-87549645, false}
                , new Object[]{DtInteger.PRIV, false}
                , new Object[]{DtInteger.ME, false}
                , new Object[]{DtInteger.MIN, true}
                , new Object[]{DtInteger.MAX, false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMinTest(int value, boolean result) {
        assertThat(DtInteger.isMin(value)).isEqualTo(result);
    }

    static Stream<Object[]> isMaxTest() {
        return Stream.of(
                new Object[]{15482, false}
                , new Object[]{-87549645, false}
                , new Object[]{DtInteger.PRIV, false}
                , new Object[]{DtInteger.ME, false}
                , new Object[]{DtInteger.MIN, false}
                , new Object[]{DtInteger.MAX, true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMaxTest(int value, boolean result) {
        assertThat(DtInteger.isMax(value)).isEqualTo(result);
    }
}