package com.provys.common.crypt;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import java.io.IOException;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class DtEncryptedStringTest {

  static Stream<Object[]> valueOfTest() {
    return Stream.of(
        new Object[]{"My first test"}
        , new Object[]{"One more test"}
        , new Object[]{"1989-21:25"}
        , new Object[]{"$ewrklnt.z#&4gsfdvxyc vy"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void valueOfTest(String value) {
    var result = DtEncryptedString.valueOf(value);
    assertThat(result.getIisValue()).startsWith("IIS$");
    assertThat(result.getValue()).isEqualTo(value);
  }

  @Test
  void valueOfTestEncrypted() {
    assertThat(
        DtEncryptedString.valueOf("IIS$qjwxPUB+Ra9XnA9wEBWg9UGzM+1C5bP0IeLh8y1fQgiAyqHYpGs=")
            .getIisValue())
        .isEqualTo("IIS$qjwxPUB+Ra9XnA9wEBWg9UGzM+1C5bP0IeLh8y1fQgiAyqHYpGs=");
  }

  @Test
  @SuppressWarnings("argument.type.incompatible")
  void valueOfTestNull() {
    assertThatCode(() -> DtEncryptedString.valueOf(null)).isInstanceOf(NullPointerException.class);
  }

  static Stream<Object[]> getValueTest() {
    return Stream.of(
        new Object[]{"IIS$fc+avr25q15WIB7bONOcNItFlohr3GsFDJR3ERv1RDJBnCG1smA=", "Some Random Test"}
        , new Object[]{"IIS$BMom+CsNbRklWuoYWaVPTalWpj+Tug==", "#/*+-&"}
        , new Object[]{"IIS$HvBTO5/1PXYfmxWAvLkKagzg7WjQzTG8PZXWMWkIKw6ACgAQdkg=", "1989-21:25"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getValueTest(String encrypted, String value) {
    var result = DtEncryptedString.valueOf(encrypted).getValue();
    assertThat(result).isEqualTo(value);
  }

  static Stream<Object[]> equalsTest() {
    return Stream.of(
        new Object[]{"UIkjKfla,w"}
        , new Object[]{"jnafOIJObhUHjKHDHEwewdskjd548Ki"}
        , new Object[]{"/LkosajasjiJUOjaiw§poKLkjeopfjke"}
        , new Object[]{"$ewrklnt.z#&4gsfdvxyc vyP/,efw"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(String value) {
    // even if encryption includes salt, equals should still work correctly
    var value1 = DtEncryptedString.valueOf(value);
    var value2 = DtEncryptedString.valueOf(value);
    assertThat(value1.equals(value2)).isTrue();
  }

  private static final class DtEncryptedStringElement {

    @JsonProperty
    private @MonotonicNonNull DtEncryptedString value;

    /**
     * @return value of field value
     */
    @Nullable DtEncryptedString getValue() {
      return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     */
    DtEncryptedStringElement setValue(DtEncryptedString value) {
      this.value = value;
      return this;
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      DtEncryptedStringElement that = (DtEncryptedStringElement) o;
      return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
  }

  static Stream<Object[]> jacksonTest() {
    return Stream.of(
        new Object[]{new DtEncryptedStringElement().setValue(
            DtEncryptedString.valueOf("IIS$qjwxPUB+Ra9XnA9wEBWg9UGzM+1C5bP0IeLh8y1fQgiAyqHYpGs=")),
            "{\"value\":\"IIS$qjwxPUB+Ra9XnA9wEBWg9UGzM+1C5bP0IeLh8y1fQgiAyqHYpGs=\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtEncryptedStringElement><value>IIS$qjwxPUB+Ra9XnA9wEBWg9UGzM+1C5bP0IeLh8y1f"
                + "QgiAyqHYpGs=</value></DtEncryptedStringElement>"}
        , new Object[]{new DtEncryptedStringElement().setValue(
            DtEncryptedString.valueOf("IIS$BMom+CsNbRklWuoYWaVPTalWpj+Tug==")),
            "{\"value\":\"IIS$BMom+CsNbRklWuoYWaVPTalWpj+Tug==\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtEncryptedStringElement><value>IIS$BMom+CsNbRklWuoYWaVPTalWpj+Tug==</value>"
                + "</DtEncryptedStringElement>"}
    );
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToJsonTest(DtEncryptedStringElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
        .isEqualTo(json);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromJsonTest(DtEncryptedStringElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(json, DtEncryptedStringElement.class))
        .isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToXmlTest(DtEncryptedStringElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
        .isEqualTo(xml);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromXmlTest(DtEncryptedStringElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(xml, DtEncryptedStringElement.class))
        .isEqualTo(value);
  }
}