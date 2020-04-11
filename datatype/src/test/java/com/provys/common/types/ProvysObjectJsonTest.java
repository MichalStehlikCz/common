package com.provys.common.types;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.provys.common.jackson.JacksonMappers;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProvysObjectJsonTest {

  private static final class ObjectElement {

    @JsonProperty
    @JsonSerialize(using = ProvysObjectSerializer.class)
    @JsonDeserialize(using = ProvysObjectDeserializer.class)
    private @MonotonicNonNull Object value;

    /**
     * @return value of field value
     */
    @Nullable Object getValue() {
      return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     */
    ObjectElement setValue(Object value) {
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
      ObjectElement that = (ObjectElement) o;
      return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
  }

  static Stream<Object[]> jacksonTest() {
    return Stream.of(
        new Object[]{new ObjectElement().setValue(5),
            "{\"value\":{\"INTEGER\":5}}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ObjectElement><value><INTEGER>5</INTEGER></value></ObjectElement>"}
        , new Object[]{new ObjectElement().setValue(5.0),
            "{\"value\":{\"NUMBER\":5.0}}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ObjectElement><value><NUMBER>5.0</NUMBER></value></ObjectElement>"}
        , new Object[]{new ObjectElement().setValue("test string"),
            "{\"value\":{\"STRING\":\"test string\"}}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ObjectElement><value><STRING>test string</STRING></value></ObjectElement>"}
    );
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToJsonTest(ObjectElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
        .isEqualTo(json);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromJsonTest(ObjectElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(json, ObjectElement.class))
        .isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToXmlTest(ObjectElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
        .isEqualTo(xml);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromXmlTest(ObjectElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(xml, ObjectElement.class))
        .isEqualTo(value);
  }
}
