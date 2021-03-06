package com.provys.common.datatype;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DtDateTimeTest {

  static Stream<Object[]> ofDateTest() {
    return Stream.of(
        new Object[]{DtDate.of(1989, 11, 25)}
        , new Object[]{DtDate.MIN}
        , new Object[]{DtDate.MAX}
        , new Object[]{DtDate.PRIV}
        , new Object[]{DtDate.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void ofDateTest(DtDate date) {
    var value = DtDateTime.ofDate(date);
    assertThat(value.getDate()).isEqualTo(date);
    if (date.isRegular()) {
      assertThat(value.getTime().getSeconds()).isEqualTo(0);
    }
  }

  static Stream<@Nullable Object[]> ofDateTimeTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(1989, 11, 25), DtTimeS.ofSeconds(58421),
            null, DtDate.of(1989, 11, 25), DtTimeS.ofSeconds(58421)}
        , new @Nullable Object[]{DtDate.of(2005, 7, 15), DtTimeS.ofDayToMinute(3, 11, 54),
            null, DtDate.of(2005, 7, 18), DtTimeS.ofHourToMinute(11, 54)}
        , new @Nullable Object[]{DtDate.MIN, DtTimeS.ofSeconds(150), null, DtDate.MIN,
            DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDate.MIN, DtTimeS.MIN, null, DtDate.MIN, DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDate.MIN, DtTimeS.MAX,
            "Cannot create datetime from min date and max time", null, null}
        , new @Nullable Object[]{DtDate.MAX, DtTimeS.ofSeconds(150), null, DtDate.MAX,
            DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDate.MAX, DtTimeS.MAX, null, DtDate.MAX, DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDate.MAX, DtTimeS.MIN,
            "Cannot create datetime from max date and min time", null, null}
        , new @Nullable Object[]{DtDate.PRIV, DtTimeS.ME, null, DtDate.PRIV, DtTimeS.PRIV}
        , new @Nullable Object[]{DtDate.of(1989, 11, 25), DtTimeS.PRIV, null, DtDate.PRIV,
            DtTimeS.PRIV}
        , new @Nullable Object[]{DtDate.ME, DtTimeS.MIN, null, DtDate.ME, DtTimeS.ME}
        , new @Nullable Object[]{DtDate.MAX, DtTimeS.ME, null, DtDate.ME, DtTimeS.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void ofDateTimeTest(DtDate date, DtTimeS time, @Nullable String message,
      @Nullable DtDate resultDate,
      @Nullable DtTimeS resultTime) {
    if (message == null) {
      var value = DtDateTime.ofDateTime(date, time);
      assertThat(value.getDate()).isEqualTo(resultDate);
      assertThat(value.getTime()).isEqualTo(resultTime);
    } else {
      assertThatThrownBy(() -> DtDateTime.ofDateTime(date, time)).hasMessage(message);
    }
  }

  static Stream<Object[]> ofLocalDateTimeTest() {
    return Stream.of(
        new Object[]{LocalDateTime.of(1989, 11, 25, 15, 20),
            DtDateTime.ofDateTime(DtDate.of(1989, 11, 25),
                DtTimeS.ofHourToMinute(15, 20))}
        , new Object[]{LocalDateTime.of(2015, 3, 8, 10, 48, 12),
            DtDateTime.of(2015, 3, 8, 10, 48, 12)}
    );
  }

  @ParameterizedTest
  @MethodSource
  void ofLocalDateTimeTest(LocalDateTime value, DtDateTime result) {
    assertThat(DtDateTime.ofLocalDateTime(value)).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> parseTest() {
    return Stream.of(
        new @Nullable Object[]{"2005-07-15T03:11:54",
            null, DtDate.of(2005, 7, 15),
            DtTimeS.ofHourToSecond(3, 11, 54)}
        , new @Nullable Object[]{"2005-07-15 03:11:54",
            "T expected as delimiter of date and time part", null, null}
        , new @Nullable Object[]{"2005-07-15T03:11:54x", "Value parsed before reading whole text",
            null, null}
        , new @Nullable Object[]{"", "Empty parser supplied to read DtDate", null, null}
        , new @Nullable Object[]{DtDateTime.MIN_TEXT, null, DtDate.MIN, DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDateTime.MAX_TEXT, null, DtDate.MAX, DtTimeS.ofSeconds(0)}
        , new @Nullable Object[]{DtDateTime.PRIV_TEXT, null, DtDate.PRIV, DtTimeS.PRIV}
        , new @Nullable Object[]{DtDateTime.ME_TEXT, null, DtDate.ME, DtTimeS.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void parseTest(String text, @Nullable String message, @Nullable DtDate resultDate,
      @Nullable DtTimeS resultTime) {
    if (message == null) {
      var value = DtDateTime.parse(text);
      assertThat(value.getDate()).isEqualTo(resultDate);
      assertThat(value.getTime()).isEqualTo(resultTime);
    } else {
      assertThatThrownBy(() -> DtDateTime.parse(text)).hasMessage(message);
    }
  }

  static Stream<Object[]> getTimeBaseTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 28, 1, 25),
            DtDate.of(1989, 11, 27), DtTimeS.ofDayToMinute(1, 1, 25)}
        , new Object[]{DtDateTime.of(1989, 11, 28, 1, 25),
            DtDate.of(1989, 11, 29), DtTimeS.ofDayToMinute(-1, 1, 25)}
        , new Object[]{DtDateTime.PRIV, DtDate.of(1989, 11, 27), DtTimeS.PRIV}
        , new Object[]{DtDateTime.of(2011, 5, 14), DtDate.PRIV, DtTimeS.PRIV}
        , new Object[]{DtDateTime.PRIV, DtDate.ME, DtTimeS.PRIV}
        , new Object[]{DtDateTime.ME, DtDate.of(1989, 11, 27), DtTimeS.ME}
        , new Object[]{DtDateTime.of(2011, 5, 14), DtDate.ME, DtTimeS.ME}
        , new Object[]{DtDateTime.ME, DtDate.MIN, DtTimeS.ME}
        , new Object[]{DtDateTime.MIN, DtDate.of(1989, 11, 27), DtTimeS.MIN}
        , new Object[]{DtDateTime.of(2011, 5, 14), DtDate.MIN, DtTimeS.MAX}
        , new Object[]{DtDateTime.MIN, DtDate.MIN, DtTimeS.ofSeconds(0)}
        , new Object[]{DtDateTime.MAX, DtDate.of(1989, 11, 27), DtTimeS.MAX}
        , new Object[]{DtDateTime.of(2011, 5, 14), DtDate.MAX, DtTimeS.MIN}
        , new Object[]{DtDateTime.MAX, DtDate.MAX, DtTimeS.ofSeconds(0)}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getTimeBaseTest(DtDateTime value, DtDate baseDate, DtTimeS result) {
    assertThat(value.getTime(baseDate)).isEqualTo(result);
  }

  static Stream<Object[]> isRegularTest() {
    return Stream.of(
        new Object[]{DtDateTime.ofDate(DtDate.of(1989, 11, 25)), true}
        , new Object[]{DtDateTime.MIN, false}
        , new Object[]{DtDateTime.MAX, false}
        , new Object[]{DtDateTime.PRIV, false}
        , new Object[]{DtDateTime.ME, false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void isRegularTest(DtDateTime value, boolean result) {
    assertThat(value.isRegular()).isEqualTo(result);
  }

  static Stream<Object[]> isValidValueTest() {
    return Stream.of(
        new Object[]{DtDateTime.ofDate(DtDate.of(1989, 11, 25)), true}
        , new Object[]{DtDateTime.MIN, true}
        , new Object[]{DtDateTime.MAX, true}
        , new Object[]{DtDateTime.PRIV, false}
        , new Object[]{DtDateTime.ME, false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void isValidValueTest(DtDateTime value, boolean result) {
    assertThat(value.isValidValue()).isEqualTo(result);
  }

  static Stream<Object[]> getLocalDateTimeTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25, 15, 20),
            LocalDateTime.of(1989, 11, 25, 15, 20)}
        , new Object[]{DtDateTime.of(2015, 3, 8, 10, 48, 12),
            LocalDateTime.of(2015, 3, 8, 10, 48, 12)}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getLocalDateTimeTest(DtDateTime value, LocalDateTime result) {
    assertThat(value.getLocalDateTime()).isEqualTo(result);
  }

  static Stream<Object[]> getYearTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25), 1989}
        , new Object[]{DtDateTime.PRIV, DtInteger.PRIV}
        , new Object[]{DtDateTime.ME, DtInteger.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getYearTest(DtDateTime value, int result) {
    assertThat(value.getYear()).isEqualTo(result);
  }

  static Stream<Object[]> getMonthValueTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25), 11}
        , new Object[]{DtDateTime.PRIV, DtInteger.PRIV}
        , new Object[]{DtDateTime.ME, DtInteger.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getMonthValueTest(DtDateTime value, int result) {
    assertThat(value.getMonthValue()).isEqualTo(result);
  }

  static Stream<Object[]> getDayOfMonthTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25), 25}
        , new Object[]{DtDateTime.PRIV, DtInteger.PRIV}
        , new Object[]{DtDateTime.ME, DtInteger.ME}
    );
  }

  @ParameterizedTest
  @MethodSource
  void getDayOfMonthTest(DtDateTime value, int result) {
    assertThat(value.getDayOfMonth()).isEqualTo(result);
  }

  static Stream<Object[]> plusDaysTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25, 10, 5), 10,
            DtDateTime.of(1989, 12, 5, 10, 5)}
        , new Object[]{DtDateTime.of(1989, 11, 25, 3, 58, 5), -10,
            DtDateTime.of(1989, 11, 15, 3, 58, 5)}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), 1,
            DtDateTime.of(2012, 1, 1, 14, 7)}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), -1,
            DtDateTime.of(2010, 12, 31, 18, 42)}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), 0,
            DtDateTime.of(2011, 1, 1, 18, 42)}
        , new Object[]{DtDateTime.PRIV, -1, DtDateTime.PRIV}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), DtInteger.PRIV,
            DtDateTime.PRIV}
        , new Object[]{DtDateTime.ME, -1, DtDateTime.ME}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), DtInteger.ME,
            DtDateTime.ME}
        , new Object[]{DtDateTime.MIN, -1, DtDateTime.MIN}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), DtInteger.MIN,
            DtDateTime.MIN}
        , new Object[]{DtDateTime.MAX, -1, DtDateTime.MAX}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), DtInteger.MAX,
            DtDateTime.MAX}
    );
  }

  @ParameterizedTest
  @MethodSource
  void plusDaysTest(DtDateTime value, int days, DtDateTime result) {
    assertThat(value.plusDays(days)).isEqualTo(result);
  }

  static Stream<Object[]> plusDaysDTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 25, 10, 5), 27d / 24d,
            DtDateTime.of(1989, 11, 26, 13, 5)}
        , new Object[]{DtDateTime.of(1989, 11, 25, 3, 58, 5), -29d / 24d,
            DtDateTime.of(1989, 11, 23, 22, 58, 5)}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), 1d + 1d / 86400d,
            DtDateTime.of(2012, 1, 1, 14, 7, 1)}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), -1d,
            DtDateTime.of(2010, 12, 31, 18, 42)}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42), 0d,
            DtDateTime.of(2011, 1, 1, 18, 42)}
        , new Object[]{DtDateTime.PRIV, -1, DtDateTime.PRIV}
        , new Object[]{DtDateTime.ME, -1, DtDateTime.ME}
        , new Object[]{DtDateTime.MIN, -1, DtDateTime.MIN}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), DtDouble.MIN,
            DtDateTime.MIN}
        , new Object[]{DtDateTime.MAX, -1, DtDateTime.MAX}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), DtDouble.MAX,
            DtDateTime.MAX}
    );
  }

  @ParameterizedTest
  @MethodSource
  void plusDaysDTest(DtDateTime value, double days, DtDateTime result) {
    assertThat(value.plusDays(days)).isEqualTo(result);
  }

  static Stream<Object[]> minusTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 26, 13, 5),
            DtDateTime.of(1989, 11, 25, 10, 5), 27d / 24d}
        , new Object[]{DtDateTime.of(1989, 11, 23, 22, 58, 5),
            DtDateTime.of(1989, 11, 25, 3, 58, 5), -29d / 24d}
        , new Object[]{DtDateTime.of(2012, 1, 1, 14, 7, 1),
            DtDateTime.of(2011, 12, 31, 14, 7), 1d + 1d / 86400d}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42),
            DtDateTime.of(2010, 12, 31, 18, 42), 1d}
        , new Object[]{DtDateTime.of(2011, 1, 1, 18, 42),
            DtDateTime.of(2011, 1, 1, 18, 42), 0d}
        , new Object[]{DtDateTime.PRIV, DtDateTime.of(2011, 1, 1, 18, 42),
            DtDouble.PRIV}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), DtDateTime.ME,
            DtDouble.ME}
        , new Object[]{DtDateTime.MIN, DtDateTime.of(2011, 12, 31, 14, 7),
            DtDouble.MIN}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), DtDateTime.MIN,
            DtDouble.MAX}
        , new Object[]{DtDateTime.MAX, DtDateTime.of(2011, 12, 31, 14, 7),
            DtDouble.MAX}
        , new Object[]{DtDateTime.of(2011, 12, 31, 14, 7), DtDateTime.MAX,
            DtDouble.MIN}
    );
  }

  @ParameterizedTest
  @MethodSource
  void minusTest(DtDateTime first, DtDateTime second, double result) {
    assertThat(first.minus(second)).isEqualTo(result);
  }

  static Stream<Object[]> toIsoTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(2012, 10, 25, 12, 25), "2012-10-25T12:25:00"}
        , new Object[]{DtDateTime.of(2025, 11, 30, 15, 57, 24),
            "2025-11-30T15:57:24"}
        , new Object[]{DtDateTime.PRIV, "1000-01-02T00:00:00"}
        , new Object[]{DtDateTime.ME, "1000-01-01T00:00:00"}
        , new Object[]{DtDateTime.MIN, "1000-01-03T00:00:00"}
        , new Object[]{DtDateTime.MAX, "5000-01-01T00:00:00"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toIsoTest(DtDateTime dateTime, String result) {
    assertThat(dateTime.toIso()).isEqualTo(result);
  }

  static Stream<Object[]> toProvysValueTest() {
    return Stream.of(
        new Object[]{DtDateTime.of(1989, 11, 26, 13, 5), "26.11.1989 13:05:00"}
        , new Object[]{DtDateTime.of(1989, 11, 23, 22, 58, 5),
            "23.11.1989 22:58:05"}
        , new Object[]{DtDateTime.PRIV, "02.01.1000 00:00:00"}
        , new Object[]{DtDateTime.ME, "01.01.1000 00:00:00"}
        , new Object[]{DtDateTime.MIN, "03.01.1000 00:00:00"}
        , new Object[]{DtDateTime.MAX, "01.01.5000 00:00:00"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toProvysValueTest(DtDateTime value, String result) {
    assertThat(value.toProvysValue()).isEqualTo(result);
  }

  @JsonRootName("DtDateTimeElement")
  private static final class DtDateTimeElement {

    @JsonProperty
    private @MonotonicNonNull DtDateTime value;

    /**
     * Value of field value.
     *
     * @return value of field value
     */
    @Nullable DtDateTime getValue() {
      return value;
    }

    /**
     * Set value of field value.
     *
     * @param value is new value to be set
     */
    DtDateTimeElement setValue(DtDateTime value) {
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
      DtDateTimeElement that = (DtDateTimeElement) o;
      return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
      return "DtDateTimeElement{"
          + "value=" + value
          + '}';
    }
  }

  static Stream<Object[]> jacksonTest() {
    return Stream.of(
        new Object[]{new DtDateTimeElement().setValue(DtDateTime.of(2008, 7, 24, 12, 5, 38)),
            "{\"value\":\"2008-07-24T12:05:38\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtDateTimeElement><value>2008-07-24T12:05:38</value></DtDateTimeElement>"}
        , new Object[]{new DtDateTimeElement().setValue(DtDateTime.PRIV),
            "{\"value\":\"1000-01-02T00:00:00\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtDateTimeElement><value>1000-01-02T00:00:00</value></DtDateTimeElement>"}
        , new Object[]{new DtDateTimeElement().setValue(DtDateTime.ME),
            "{\"value\":\"1000-01-01T00:00:00\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtDateTimeElement><value>1000-01-01T00:00:00</value></DtDateTimeElement>"}
        , new Object[]{new DtDateTimeElement().setValue(DtDateTime.MIN),
            "{\"value\":\"1000-01-03T00:00:00\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtDateTimeElement><value>1000-01-03T00:00:00</value></DtDateTimeElement>"}
        , new Object[]{new DtDateTimeElement().setValue(DtDateTime.MAX),
            "{\"value\":\"5000-01-01T00:00:00\"}",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<DtDateTimeElement><value>5000-01-01T00:00:00</value></DtDateTimeElement>"}
    );
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToJsonTest(DtDateTimeElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
        .isEqualTo(json);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromJsonTest(DtDateTimeElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(json, DtDateTimeElement.class))
        .isEqualTo(value);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void serializeToXmlTest(DtDateTimeElement value, String json, String xml)
      throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
        .isEqualTo(xml);
  }

  @ParameterizedTest
  @MethodSource("jacksonTest")
  void deserializeFromXmlTest(DtDateTimeElement value, String json, String xml)
      throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(xml, DtDateTimeElement.class))
        .isEqualTo(value);
  }
}