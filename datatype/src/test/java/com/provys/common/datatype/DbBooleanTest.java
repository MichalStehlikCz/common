package com.provys.common.datatype;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class DbBooleanTest {

  private static final class DbBooleanElement {

    @JsonProperty
    private @MonotonicNonNull DbBoolean value;

    /**
     * @return value of field value
     */
    @Nullable DbBoolean getValue() {
      return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     */
    DbBooleanElement setValue(DbBoolean value) {
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
      DbBooleanElement that = (DbBooleanElement) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
  }

  static Stream<Object[]> jacksonTest() {
    return Stream.of(
        new Object[]{new DbBooleanElement().setValue(DbBoolean.TRUE),
            "{\"value\":true}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DbBooleanElement><value>true</value></DbBooleanElement>"}
        , new Object[]{new DbBooleanElement().setValue(DbBoolean.FALSE),
            "{\"value\":false}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DbBooleanElement><value>false</value></DbBooleanElement>"}
    );
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToJsonTest(DbBooleanElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
        .isEqualTo(json);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromJsonTest(DbBooleanElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(json, DbBooleanElement.class))
        .isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToXmlTest(DbBooleanElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
        .isEqualTo(xml);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromXmlTest(DbBooleanElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(xml, DbBooleanElement.class))
        .isEqualTo(value);
  }
}