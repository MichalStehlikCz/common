package com.provys.common.datatype;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DtBooleanTest {

  @Test
  void parseProvysValueTest() {
    assertThat(DtBoolean.ofProvysDb("Y")).isTrue();
    assertThat(DtBoolean.ofProvysDb("N")).isFalse();
    assertThatThrownBy(() -> DtBoolean.ofProvysDb("0"));
  }

  @Test
  void toProvysValueTest() {
    assertThat(DtBoolean.toProvysDb(true)).isEqualTo("Y");
    assertThat(DtBoolean.toProvysDb(false)).isEqualTo("N");

  }
}