package com.provys.common.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.provys.common.jackson.JacksonMappers;
import java.io.IOException;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ProvysClassJsonTest {

  private static final class ClassElement {

    @JsonProperty
    @JsonSerialize(using = ProvysClassSerializer.class)
    @JsonDeserialize(using = ProvysClassDeserializer.class)
    private @MonotonicNonNull Class<?> value;

    /**
     * @return value of field value
     */
    @Nullable Class<?> getValue() {
      return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     */
    ClassElement setValue(Class<?> value) {
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
      ClassElement that = (ClassElement) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
  }

  static Stream<Object[]> jacksonTest() {
    return Stream.of(
        new Object[]{new ClassElement().setValue(Integer.class),
            "{\"value\":\"INTEGER\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ClassElement><value>INTEGER</value></ClassElement>"}
        , new Object[]{new ClassElement().setValue(Double.class),
            "{\"value\":\"NUMBER\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ClassElement><value>NUMBER</value></ClassElement>"}
        , new Object[]{new ClassElement().setValue(String.class),
            "{\"value\":\"STRING\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ClassElement><value>STRING</value></ClassElement>"}
    );
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToJsonTest(ClassElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
        .isEqualTo(json);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromJsonTest(ClassElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(json, ClassElement.class))
        .isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToXmlTest(ClassElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
        .isEqualTo(xml);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromXmlTest(ClassElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(xml, ClassElement.class))
        .isEqualTo(value);
  }
}
