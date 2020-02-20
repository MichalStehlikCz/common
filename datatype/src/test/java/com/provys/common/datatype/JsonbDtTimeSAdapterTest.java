package com.provys.common.datatype;

import org.assertj.core.api.Fail;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonbDtTimeSAdapterTest {

  static Stream<@Nullable Object[]> toJsonTest() {
    return Stream.of(
        new @Nullable Object[]{DtTimeS.ofHourToSecond(12, 25, 25), "\"12:25:25\""}
        , new @Nullable Object[]{DtTimeS.PRIV, null}
        , new @Nullable Object[]{DtTimeS.ME, null}
        , new @Nullable Object[]{DtTimeS.MIN, null}
        , new @Nullable Object[]{DtTimeS.MAX, null}
    );
  }

  @ParameterizedTest
  @MethodSource
  void toJsonTest(DtTimeS value, @Nullable String result) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      if (result == null) {
        assertThatThrownBy(() -> jsonb.toJson(value));
      } else {
        assertThat(jsonb.toJson(value)).isEqualTo(result);
      }
    } catch (Exception e) {
      Fail.fail("Failed to close Jsonb");
    }
  }

  static Stream<Object[]> fromJsonTest() {
    return Stream.of(
        new Object[]{"\"12:25:25\"", DtTimeS.ofHourToSecond(12, 25, 25)}
        , new Object[]{"\"24:00:00\"", DtTimeS.ofHourToSecond(24, 0, 0)}
    );
  }

  @ParameterizedTest
  @MethodSource
  void fromJsonTest(String value, DtTimeS result) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      assertThat(jsonb.fromJson(value, DtTimeS.class)).isEqualTo(result);
    } catch (Exception e) {
      Fail.fail("Failed to close Jsonb");
    }
  }
}
