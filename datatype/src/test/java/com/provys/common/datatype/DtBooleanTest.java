package com.provys.common.datatype;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DtBooleanTest {

    @Test
    void parseProvysValueTest() {
        assertThat(DtBoolean.parseProvysValue('Y')).isTrue();
        assertThat(DtBoolean.parseProvysValue('N')).isFalse();
        assertThatThrownBy(() -> DtBoolean.parseProvysValue('0'));
    }

    @Test
    void toProvysValueTest() {
        assertThat(DtBoolean.toProvysValue(true)).isEqualTo('Y');
        assertThat(DtBoolean.toProvysValue(false)).isEqualTo('N');

    }
}