package com.provys.common.datatype;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DtStringTest {

  @Test
  void isRegularTest() {
    assertThat(DtString.isRegular("abcd")).isTrue();
    assertThat(DtString.isRegular("##########")).isFalse();
    assertThat(DtString.isRegular("**********")).isFalse();
  }

  @Test
  void isPrivTest() {
    assertThat(DtString.isPriv("abcd")).isFalse();
    assertThat(DtString.isPriv("##########")).isTrue();
    assertThat(DtString.isPriv("**********")).isFalse();
  }

  @Test
  void isMETest() {
    assertThat(DtString.isME("abcd")).isFalse();
    assertThat(DtString.isME("##########")).isFalse();
    assertThat(DtString.isME("**********")).isTrue();
  }
}