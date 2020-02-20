package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.provys.common.jackson.JacksonMappers;
import org.assertj.core.api.Fail;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtDateTest {

  @Test
  void ofLocalDateTest() {
    var value = LocalDate.of(1989, 11, 5);
    assertThat(DtDate.ofLocalDate(value).getLocalDate()).isEqualTo(value);
  }

  @Test
  @SuppressWarnings("nullness")
    // we want to test passing null even though it is not allowed by checker framework
  void ofLocalDateNullTest() {
    assertThatThrownBy(() -> DtDate.ofLocalDate(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void ofTest() {
    int year = 2014;
    short month = 5;
    short day = 30;
    assertThat(DtDate.of(year, month, day).getLocalDate())
        .isEqualTo(LocalDate.of(year, month, day));
  }

  @Test
  void ofTest2() {
    int year = 2019;
    int month = 1;
    int day = 31;
    assertThat(DtDate.of(year, month, day).getLocalDate())
        .isEqualTo(LocalDate.of(year, month, day));
  }

  @Test
  void ofInstant() {
    Instant instant = Instant.parse("2019-01-12T10:51:13Z");
    ZoneId zone = ZoneId.of("GMT");
    assertThat(DtDate.ofInstant(instant, zone)).isEqualTo(DtDate.of(2019, 1, 12));
  }

  @Test
  void ofInstant2() {
    Instant instant = Instant.parse("2015-12-07T10:51:13Z");
    ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
    assertThat(DtDate.ofInstant(instant))
        .isEqualTo(DtDate.of(dateTime.getYear(), dateTime.getMonthValue(),
            dateTime.getDayOfMonth()));
  }

  static Stream<@Nullable Object[]> parseTest() {
    return Stream.of(
        new @Nullable Object[]{"1989-11-25", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989:21:25", null}
        , new @Nullable Object[]{"1989-21:25", null}
        , new @Nullable Object[]{"1989-21", null}
        , new @Nullable Object[]{"1989-1-1", null}
        , new @Nullable Object[]{"1989-11-25Z", null}
    );
  }

  @ParameterizedTest
  @MethodSource
  void parseTest(String date, @Nullable DtDate result) {
    if (result != null) {
      assertThat(DtDate.parse(date)).isEqualTo(result);
    } else {
      assertThatThrownBy(() -> DtDate.parse(date)).isInstanceOf(DateTimeParseException.class);
    }
  }

  static Stream<@Nullable Object[]> parseIsoTest() {
    return Stream.of(
        new @Nullable Object[]{"1989-11-25", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989:21:25", null}
        , new @Nullable Object[]{"1989-21:25", null}
        , new @Nullable Object[]{"1989-21", null}
        , new @Nullable Object[]{"1989-1-1", null}
        , new @Nullable Object[]{"1989-11-25Z", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989-11-25T00:00:00.000Z", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989-11-25T00:00:00,000Z", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989-11-25T00:00:00Z", DtDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989-11-25T00:00Z", DtDate.of(1989, 11, 25)}
    );
  }

  @ParameterizedTest
  @MethodSource
  void parseIsoTest(String date, @Nullable DtDate result) {
    if (result != null) {
      assertThat(DtDate.parseIso(date)).isEqualTo(result);
    } else {
      assertThatThrownBy(() -> DtDate.parse(date)).isInstanceOf(DateTimeParseException.class);
    }
  }

  static Stream<@Nullable Object[]> ofProvysValueTest() {
    return Stream.of(
        new @Nullable Object[]{"25.11.1989", LocalDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"25.11.1989 00:00:00", LocalDate.of(1989, 11, 25)}
        , new @Nullable Object[]{"1989-01-01", null}
        , new @Nullable Object[]{"25.1.2018", null}
        , new @Nullable Object[]{"2.01.2018", null}
        , new @Nullable Object[]{"02.01.2018 05:00:00", null}
        , new @Nullable Object[]{"02.01.2018 00:00", null}
        , new @Nullable Object[]{null, null}
    );
  }

  @ParameterizedTest
  @MethodSource
  void ofProvysValueTest(String date, @Nullable LocalDate result) {
    if (result != null) {
      assertThat(DtDate.ofProvysValue(date)).isEqualTo(DtDate.ofLocalDate(result));
    } else {
      assertThatThrownBy(() -> DtDate.ofProvysValue(date));
    }
  }

  static Stream<@Nullable Object[]> isRegularTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(1989, 11, 25), true}
        , new @Nullable Object[]{DtDate.MIN, false}
        , new @Nullable Object[]{DtDate.MAX, false}
        , new @Nullable Object[]{DtDate.PRIV, false}
        , new @Nullable Object[]{DtDate.ME, false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void isRegularTest(DtDate date, boolean result) {
    assertThat(date.isRegular()).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> isValidValueTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(1989, 11, 25), true}
        , new @Nullable Object[]{DtDate.MIN, true}
        , new @Nullable Object[]{DtDate.MAX, true}
        , new @Nullable Object[]{DtDate.PRIV, false}
        , new @Nullable Object[]{DtDate.ME, false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void isValidValueTest(DtDate date, boolean result) {
    assertThat(date.isValidValue()).isEqualTo(result);
  }

  @Test
  void getYearTest() {
    assertThat(DtDate.of(1987, 12, 5).getYear()).isEqualTo(1987);
  }

  @Test
  void getMonthValueTest() {
    assertThat(DtDate.of(1987, 12, 5).getMonthValue()).isEqualTo(12);
  }

  @Test
  void getDayOfMonthTest() {
    assertThat(DtDate.of(1987, 12, 5).getDayOfMonth()).isEqualTo(5);
  }

  static Stream<@Nullable Object[]> plusDaysTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(1989, 11, 25), 10,
            DtDate.of(1989, 12, 5)}
        , new @Nullable Object[]{DtDate.of(1989, 11, 25), -10,
            DtDate.of(1989, 11, 15)}
        , new @Nullable Object[]{DtDate.of(2011, 12, 31), 1,
            DtDate.of(2012, 1, 1)}
        , new @Nullable Object[]{DtDate.of(2011, 1, 1), -1,
            DtDate.of(2010, 12, 31)}
        , new @Nullable Object[]{DtDate.PRIV, -1, DtDate.PRIV}
        , new @Nullable Object[]{DtDate.ME, -1, DtDate.ME}
        , new @Nullable Object[]{DtDate.MIN, -1, DtDate.MIN}
        , new @Nullable Object[]{DtDate.MAX, -1, DtDate.MAX}
    );
  }

  @ParameterizedTest
  @MethodSource
  void plusDaysTest(DtDate date, int days, DtDate result) {
    assertThat(date.plusDays(days)).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> minusTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(1989, 12, 5), DtDate.of(1989, 11, 25), 10}
        , new @Nullable Object[]{DtDate.of(1989, 11, 15), DtDate.of(1989, 11, 25), -10}
        , new @Nullable Object[]{DtDate.of(2012, 1, 1), DtDate.of(2011, 12, 31), 1}
        , new @Nullable Object[]{DtDate.of(2010, 12, 31), DtDate.of(2011, 1, 1), -1}
        , new @Nullable Object[]{DtDate.of(2010, 12, 31), DtDate.PRIV, DtInteger.PRIV}
        , new @Nullable Object[]{DtDate.PRIV, DtDate.ME, DtInteger.PRIV}
        , new @Nullable Object[]{DtDate.ME, DtDate.of(2012, 1, 1), DtInteger.ME}
        , new @Nullable Object[]{DtDate.MIN, DtDate.of(2010, 12, 31), DtInteger.MIN}
        , new @Nullable Object[]{DtDate.MAX, DtDate.of(2010, 12, 31), DtInteger.MAX}
        , new @Nullable Object[]{DtDate.of(2010, 12, 31), DtDate.MIN, DtInteger.MAX}
        , new @Nullable Object[]{DtDate.of(2010, 12, 31), DtDate.MAX, DtInteger.MIN}
    );
  }

  @ParameterizedTest
  @MethodSource
  void minusTest(DtDate date, DtDate second, int result) {
    assertThat(date.minus(second)).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> toIsoTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.of(2012, 10, 25), "2012-10-25"}
        , new @Nullable Object[]{DtDate.of(2025, (short) 11, (short) 30), "2025-11-30"}
        , new @Nullable Object[]{DtDate.PRIV, "1000-01-02"}
        , new @Nullable Object[]{DtDate.ME, "1000-01-01"}
        , new @Nullable Object[]{DtDate.MIN, "1000-01-03"}
        , new @Nullable Object[]{DtDate.MAX, "5000-01-01"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toIsoTest(DtDate date, String result) {
    assertThat(date.toIso()).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> toProvysValueTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
            "25.11.1989"}
        , new @Nullable Object[]{DtDate.of(2012, (short) 11, (short) 30), "30.11.2012"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toProvysValueTest(DtDate date, String result) {
    assertThat(date.toProvysValue()).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> equalsTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
            DtDate.of(1989, (short) 11, (short) 25), true}
        , new @Nullable Object[]{DtDate.of(1989, (short) 11, (short) 25), null, false}
        , new @Nullable Object[]{DtDate.of(1989, (short) 11, (short) 25), "1989-11-25", false}
        , new @Nullable Object[]{DtDate.of(1989, (short) 11, (short) 25),
            DtDate.of(1989, (short) 11, (short) 24), false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void equalsTest(DtDate date, @Nullable Object other, boolean equal) {
    assertThat(date.equals(other)).isEqualTo(equal);
  }

  @Test
  void hashCodeTest() {
    assertThat(DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)).hashCode()).
        isEqualTo(DtDate.of(1989, (short) 11, (short) 25).hashCode());
  }

  static Stream<@Nullable Object[]> compareToTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
            DtDate.of(1989, (short) 11, (short) 25), 0}
        , new @Nullable Object[]{DtDate.of(1989, (short) 11, (short) 25),
            DtDate.of(1989, (short) 11, (short) 24), 1}
        , new @Nullable Object[]{DtDate.of(1989, (short) 10, (short) 25),
            DtDate.of(1989, (short) 11, (short) 24), -1}
    );
  }

  @ParameterizedTest
  @MethodSource
  void compareToTest(Comparable<? super DtDate> date, DtDate other, int result) {
    assertThat(date.compareTo(other)).isEqualTo(result);
  }

  static Stream<@Nullable Object[]> toStringTest() {
    return Stream.of(
        new @Nullable Object[]{DtDate.ofLocalDate(LocalDate.of(1989, 11, 25)),
            "1989-11-25"}
        , new @Nullable Object[]{DtDate.of(2012, (short) 11, (short) 30), "2012-11-30"}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toStringTest(DtDate date, String result) {
    assertThat(date.toString()).isEqualTo(result);
  }

  @XmlRootElement(name = "DtDateElement")
  private static class DtDateElement {

    private @MonotonicNonNull DtDate value;

    /**
     * @return value of field value
     */
    @XmlElement
    @Nullable DtDate getValue() {
      return value;
    }

    /**
     * Set value of field value
     *
     * @param value is new value to be set
     */
    DtDateElement setValue(DtDate value) {
      this.value = value;
      return this;
    }
  }

  @Test
  void serializeToJson() {
    try {
      var value = new DtDateElement().setValue(DtDate.of(2018, 5, 12));
      assertThat(JacksonMappers.getJsonMapper().writeValueAsString(value))
          .isEqualTo("{\"value\":\"2018-05-12\"}");
    } catch (JsonMappingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonMappingException thrown during test", e);
    } catch (JsonProcessingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonProcessingException thrown during test", e);
    }
  }

  @Test
  void deserializeFromJson() {
    try {
      DtDateElement result = JacksonMappers.getJsonMapper().readValue(
          "{\"value\":\"2018-05-12\"}", DtDateElement.class);
      assertThat(result.getValue()).isEqualTo(DtDate.of(2018, 5, 12));
    } catch (JsonMappingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonMappingException thrown during test", e);
    } catch (JsonProcessingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonProcessingException thrown during test", e);
    }
  }

  @Test
  void serializeToXml() {
    try {
      var value = new DtDateElement().setValue(DtDate.of(2018, 5, 12));
      assertThat(JacksonMappers.getXmlMapper().writeValueAsString(value))
          .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
              "<DtDateElement><value>2018-05-12</value></DtDateElement>");
    } catch (JsonMappingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonMappingException thrown during test", e);
    } catch (JsonProcessingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonProcessingException thrown during test", e);
    }
  }

  @Test
  void deserializeFromXml() {
    try {
      DtDateElement result = JacksonMappers.getXmlMapper().readValue(
          "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
              "<DtDateElement><value>2018-05-12</value></DtDateElement>", DtDateElement.class);
      assertThat(result.getValue()).isEqualTo(DtDate.of(2018, 5, 12));
    } catch (JsonMappingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonMappingException thrown during test", e);
    } catch (JsonProcessingException e) {
      //noinspection ResultOfMethodCallIgnored
      Fail.fail("JsonProcessingException thrown during test", e);
    }
  }
}