package com.provys.common.datatype;

import org.assertj.core.api.Fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JsonbDtDateAdapterTest {

  static Stream<Object[]> toJsonTest() {
    return Stream.of(
        new Object[]{DtDate.of(1989, 11, 25), "\"1989-11-25\""}
        , new Object[]{DtDate.PRIV, "\"1000-01-02\""}
        , new Object[]{DtDate.ME, "\"1000-01-01\""}
        , new Object[]{DtDate.MIN, "\"1000-01-03\""}
        , new Object[]{DtDate.MAX, "\"5000-01-01\""}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toJsonTest(DtDate value, String result) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      assertThat(jsonb.toJson(value)).isEqualTo(result);
    } catch (Exception e) {
      Fail.fail("Failed to close Jsonb with error " + e);
    }
  }

  static Stream<Object[]> fromJsonTest() {
    return Stream.of(
        new Object[]{"\"1989-11-25\"", DtDate.of(1989, 11, 25)}
        , new Object[]{"\"1000-01-02\"", DtDate.PRIV}
        , new Object[]{"\"1000-01-01\"", DtDate.ME}
        , new Object[]{"\"1000-01-03\"", DtDate.MIN}
        , new Object[]{"\"5000-01-01\"", DtDate.MAX}
    );
  }

  @ParameterizedTest
  @MethodSource
  void fromJsonTest(String value, DtDate result) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      assertThat(jsonb.fromJson(value, DtDate.class)).isEqualTo(result);
    } catch (Exception e) {
      Fail.fail("Failed to close Jsonb with error " + e);
    }
  }
}