package com.provys.common.datatype;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonbDtTimeSAdapterTest {

    @Nonnull
    static Stream<Object[]> toJsonTest() {
        return Stream.of(
                new Object[]{DtTimeS.ofHourToSecond(12, 25, 25), "\"12:25:25\""}
                , new Object[]{DtTimeS.PRIV, null}
                , new Object[]{DtTimeS.ME, null}
                , new Object[]{DtTimeS.MIN, null}
                , new Object[]{DtTimeS.MAX, null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toJsonTest(DtTimeS value, @Nullable String result) {
        Jsonb jsonb = JsonbBuilder.create();
        if (result == null) {
            assertThatThrownBy(() -> jsonb.toJson(value));
        } else {
            assertThat(jsonb.toJson(value)).isEqualTo(result);
        }
    }

    @Nonnull
    static Stream<Object[]> fromJsonTest() {
        return Stream.of(
                new Object[]{"\"12:25:25\"", DtTimeS.ofHourToSecond(12, 25, 25)}
                , new Object[]{"\"24:00:00\"", DtTimeS.ofHourToSecond(24, 0, 0)}
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromJsonTest(String value, DtTimeS result) {
        Jsonb jsonb = JsonbBuilder.create();
        assertThat(jsonb.fromJson(value, DtTimeS.class)).isEqualTo(result);
    }
}
