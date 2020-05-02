package com.provys.common.types;

import static org.assertj.core.api.Assertions.*;

import com.provys.common.datatype.DbBoolean;
import com.provys.common.datatype.DtDate;
import com.provys.common.datatype.DtDateTime;
import com.provys.common.datatype.DtUid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class TypeMapImplTest {

  static Stream<Object[]> isAssignableFromTest() {
    return Stream.of(
        new Object[]{Byte.class, BigDecimal.class, true}
        , new Object[]{Short.class, BigDecimal.class, true}
        , new Object[]{Integer.class, BigDecimal.class, true}
        , new Object[]{Long.class, BigDecimal.class, true}
        , new Object[]{Float.class, BigDecimal.class, true}
        , new Object[]{Double.class, BigDecimal.class, true}
        , new Object[]{BigInteger.class, BigDecimal.class, true}
        , new Object[]{BigDecimal.class, BigDecimal.class, true}
        , new Object[]{DtDate.class, BigDecimal.class, false}
        , new Object[]{DtDateTime.class, BigDecimal.class, false}
        , new Object[]{LocalDate.class, BigDecimal.class, false}
        , new Object[]{LocalDateTime.class, BigDecimal.class, false}
        , new Object[]{DtUid.class, BigDecimal.class, false}
        , new Object[]{Boolean.class, BigDecimal.class, false}
        , new Object[]{DbBoolean.class, BigDecimal.class, false}
        , new Object[]{String.class, BigDecimal.class, false}
        , new Object[]{Byte.class, BigInteger.class, true}
        , new Object[]{Short.class, BigInteger.class, true}
        , new Object[]{Integer.class, BigInteger.class, true}
        , new Object[]{Long.class, BigInteger.class, true}
        , new Object[]{Float.class, BigInteger.class, false}
        , new Object[]{Double.class, BigInteger.class, false}
        , new Object[]{BigInteger.class, BigInteger.class, true}
        , new Object[]{BigDecimal.class, BigInteger.class, false}
        , new Object[]{DtDate.class, BigInteger.class, false}
        , new Object[]{DtDateTime.class, BigInteger.class, false}
        , new Object[]{LocalDate.class, BigInteger.class, false}
        , new Object[]{LocalDateTime.class, BigInteger.class, false}
        , new Object[]{DtUid.class, BigInteger.class, false}
        , new Object[]{Boolean.class, BigInteger.class, false}
        , new Object[]{DbBoolean.class, BigInteger.class, false}
        , new Object[]{String.class, BigInteger.class, false}
        , new Object[]{Byte.class, Boolean.class, false}
        , new Object[]{Short.class, Boolean.class, false}
        , new Object[]{Integer.class, Boolean.class, false}
        , new Object[]{Long.class, Boolean.class, false}
        , new Object[]{Float.class, Boolean.class, false}
        , new Object[]{Double.class, Boolean.class, false}
        , new Object[]{BigInteger.class, Boolean.class, false}
        , new Object[]{BigDecimal.class, Boolean.class, false}
        , new Object[]{DtDate.class, Boolean.class, false}
        , new Object[]{DtDateTime.class, Boolean.class, false}
        , new Object[]{LocalDate.class, Boolean.class, false}
        , new Object[]{LocalDateTime.class, Boolean.class, false}
        , new Object[]{DtUid.class, Boolean.class, false}
        , new Object[]{Boolean.class, Boolean.class, true}
        , new Object[]{DbBoolean.class, Boolean.class, true}
        , new Object[]{String.class, Boolean.class, false}
        , new Object[]{Byte.class, Byte.class, true}
        , new Object[]{Short.class, Byte.class, false}
        , new Object[]{Integer.class, Byte.class, false}
        , new Object[]{Long.class, Byte.class, false}
        , new Object[]{Float.class, Byte.class, false}
        , new Object[]{Double.class, Byte.class, false}
        , new Object[]{BigInteger.class, Byte.class, false}
        , new Object[]{BigDecimal.class, Byte.class, false}
        , new Object[]{DtDate.class, Byte.class, false}
        , new Object[]{DtDateTime.class, Byte.class, false}
        , new Object[]{LocalDate.class, Byte.class, false}
        , new Object[]{LocalDateTime.class, Byte.class, false}
        , new Object[]{DtUid.class, Byte.class, false}
        , new Object[]{Boolean.class, Byte.class, false}
        , new Object[]{DbBoolean.class, Byte.class, false}
        , new Object[]{String.class, Byte.class, false}
        , new Object[]{Byte.class, Double.class, true}
        , new Object[]{Short.class, Double.class, true}
        , new Object[]{Integer.class, Double.class, true}
        , new Object[]{Long.class, Double.class, false}
        , new Object[]{Float.class, Double.class, true}
        , new Object[]{Double.class, Double.class, true}
        , new Object[]{BigInteger.class, Double.class, false}
        , new Object[]{BigDecimal.class, Double.class, false}
        , new Object[]{DtDate.class, Double.class, false}
        , new Object[]{DtDateTime.class, Double.class, false}
        , new Object[]{LocalDate.class, Double.class, false}
        , new Object[]{LocalDateTime.class, Double.class, false}
        , new Object[]{DtUid.class, Double.class, false}
        , new Object[]{Boolean.class, Double.class, false}
        , new Object[]{DbBoolean.class, Double.class, false}
        , new Object[]{String.class, Double.class, false}
        , new Object[]{Byte.class, DtDate.class, false}
        , new Object[]{Short.class, DtDate.class, false}
        , new Object[]{Integer.class, DtDate.class, false}
        , new Object[]{Long.class, DtDate.class, false}
        , new Object[]{Float.class, DtDate.class, false}
        , new Object[]{Double.class, DtDate.class, false}
        , new Object[]{BigInteger.class, DtDate.class, false}
        , new Object[]{BigDecimal.class, DtDate.class, false}
        , new Object[]{DtDate.class, DtDate.class, true}
        , new Object[]{DtDateTime.class, DtDate.class, true}
        , new Object[]{LocalDate.class, DtDate.class, true}
        , new Object[]{LocalDateTime.class, DtDate.class, true}
        , new Object[]{DtUid.class, DtDate.class, false}
        , new Object[]{Boolean.class, DtDate.class, false}
        , new Object[]{DbBoolean.class, DtDate.class, false}
        , new Object[]{String.class, DtDate.class, false}
        , new Object[]{Byte.class, DtDateTime.class, false}
        , new Object[]{Short.class, DtDateTime.class, false}
        , new Object[]{Integer.class, DtDateTime.class, false}
        , new Object[]{Long.class, DtDateTime.class, false}
        , new Object[]{Float.class, DtDateTime.class, false}
        , new Object[]{Double.class, DtDateTime.class, false}
        , new Object[]{BigInteger.class, DtDateTime.class, false}
        , new Object[]{BigDecimal.class, DtDateTime.class, false}
        , new Object[]{DtDate.class, DtDateTime.class, false}
        , new Object[]{DtDateTime.class, DtDateTime.class, true}
        , new Object[]{LocalDate.class, DtDateTime.class, false}
        , new Object[]{LocalDateTime.class, DtDateTime.class, true}
        , new Object[]{DtUid.class, DtDateTime.class, false}
        , new Object[]{Boolean.class, DtDateTime.class, false}
        , new Object[]{DbBoolean.class, DtDateTime.class, false}
        , new Object[]{String.class, DtDateTime.class, false}
        , new Object[]{Byte.class, Integer.class, true}
        , new Object[]{Short.class, Integer.class, true}
        , new Object[]{Integer.class, Integer.class, true}
        , new Object[]{Long.class, Integer.class, false}
        , new Object[]{Float.class, Integer.class, false}
        , new Object[]{Double.class, Integer.class, false}
        , new Object[]{BigInteger.class, Integer.class, false}
        , new Object[]{BigDecimal.class, Integer.class, false}
        , new Object[]{DtDate.class, Integer.class, false}
        , new Object[]{DtDateTime.class, Integer.class, false}
        , new Object[]{LocalDate.class, Integer.class, false}
        , new Object[]{LocalDateTime.class, Integer.class, false}
        , new Object[]{DtUid.class, Integer.class, false}
        , new Object[]{Boolean.class, Integer.class, false}
        , new Object[]{DbBoolean.class, Integer.class, false}
        , new Object[]{String.class, Integer.class, false}
    );
  }

  @ParameterizedTest
  @MethodSource
  void isAssignableFromTest(Class<?> sourceType, Class<?> targetType, boolean result) {
    assertThat(TypeMapImpl.getDefault().isAssignableFrom(targetType, sourceType))
        .isEqualTo(result);
  }

  @Test
  void convertNullTest() {
    assertThat(TypeMapImpl.getDefault().convert(BigDecimal.class, null))
        .isNull();
  }

  static Stream<@Nullable Object[]> convertTest() {
    return Stream.of(
        new @Nullable Object[]{BigDecimal.class, (byte) 15, BigDecimal.valueOf(15L)}
        , new @Nullable Object[]{BigDecimal.class, (short) 1561, BigDecimal.valueOf(1561L)}
        , new @Nullable Object[]{BigDecimal.class, 1569852, BigDecimal.valueOf(1569852L)}
        , new @Nullable Object[]{BigDecimal.class, 15474846464646L,
            BigDecimal.valueOf(15474846464646L)}
        , new @Nullable Object[]{BigDecimal.class, 15645.786f, BigDecimal.valueOf(15645.786f)}
        , new @Nullable Object[]{BigDecimal.class, 15456864.12565468,
            BigDecimal.valueOf(15456864.12565468)}
        , new @Nullable Object[]{BigDecimal.class, new BigInteger("2516548487843546897665644"),
            new BigDecimal("2516548487843546897665644")}
        , new @Nullable Object[]{BigDecimal.class,
            new BigDecimal("2516548487843546897665644.541864789"),
            new BigDecimal("2516548487843546897665644.541864789")}
        , new @Nullable Object[]{BigDecimal.class, DtDate.of(2015, 4, 12), null}
        , new @Nullable Object[]{BigDecimal.class, DtDateTime.of(2022, 12, 5, 10, 24), null}
        , new @Nullable Object[]{BigDecimal.class, LocalDate.of(1995, 5, 16), null}
        , new @Nullable Object[]{BigDecimal.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5), null}
        , new @Nullable Object[]{BigDecimal.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{BigDecimal.class, true, null}
        , new @Nullable Object[]{BigDecimal.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{BigDecimal.class, "Test string", null}
        , new @Nullable Object[]{BigInteger.class, (byte) 15, BigInteger.valueOf(15L)}
        , new @Nullable Object[]{BigInteger.class, (short) 1561, BigInteger.valueOf(1561L)}
        , new @Nullable Object[]{BigInteger.class, 1569852, BigInteger.valueOf(1569852L)}
        , new @Nullable Object[]{BigInteger.class, 15474846464646L,
            BigInteger.valueOf(15474846464646L)}
        , new @Nullable Object[]{BigInteger.class, 15645f, BigInteger.valueOf(15645L)}
        , new @Nullable Object[]{BigInteger.class, 15645.786f, null}
        , new @Nullable Object[]{BigInteger.class, 15456864, BigInteger.valueOf(15456864L)}
        , new @Nullable Object[]{BigInteger.class, 15456864.12565468, null}
        , new @Nullable Object[]{BigInteger.class, new BigInteger("2516548487843546897665644"),
            new BigInteger("2516548487843546897665644")}
        , new @Nullable Object[]{BigInteger.class,
            new BigDecimal("2516548487843546897665644.541864789"), null}
        , new @Nullable Object[]{BigInteger.class, new BigDecimal("2516548487843546897665644"),
            new BigInteger("2516548487843546897665644")}
        , new @Nullable Object[]{BigInteger.class, DtDate.of(2015, 4, 12), null}
        , new @Nullable Object[]{BigInteger.class, DtDateTime.of(2022, 12, 5, 10, 24), null}
        , new @Nullable Object[]{BigInteger.class, LocalDate.of(1995, 5, 16), null}
        , new @Nullable Object[]{BigInteger.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5), null}
        , new @Nullable Object[]{BigInteger.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{BigInteger.class, true, null}
        , new @Nullable Object[]{BigInteger.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{BigInteger.class, "Test string", null}
        , new @Nullable Object[]{Boolean.class, (byte) 15, null}
        , new @Nullable Object[]{Boolean.class, (short) 1561, null}
        , new @Nullable Object[]{Boolean.class, 1569852, null}
        , new @Nullable Object[]{Boolean.class, 15474846464646L, null}
        , new @Nullable Object[]{Boolean.class, 15645.786f, null}
        , new @Nullable Object[]{Boolean.class, 15456864.12565468, null}
        , new @Nullable Object[]{Boolean.class, new BigInteger("2516548487843546897665644"), null}
        , new @Nullable Object[]{Boolean.class,
            new BigDecimal("2516548487843546897665644.541864789"), null}
        , new @Nullable Object[]{Boolean.class, DtDate.of(2015, 4, 12), null}
        , new @Nullable Object[]{Boolean.class, DtDateTime.of(2022, 12, 5, 10, 24), null}
        , new @Nullable Object[]{Boolean.class, LocalDate.of(1995, 5, 16), null}
        , new @Nullable Object[]{Boolean.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5), null}
        , new @Nullable Object[]{Boolean.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{Boolean.class, true, true}
        , new @Nullable Object[]{Boolean.class, DbBoolean.FALSE, false}
        , new @Nullable Object[]{Boolean.class, "Test string", null}
        , new @Nullable Object[]{Double.class, (byte) 15, 15d}
        , new @Nullable Object[]{Double.class, (short) 1561, 1561d}
        , new @Nullable Object[]{Double.class, 1569852, 1569852d}
        , new @Nullable Object[]{Double.class, 15474846464646L, 15474846464646d}
        , new @Nullable Object[]{Double.class, 15645.786f, (double) 15645.786f}
        , new @Nullable Object[]{Double.class, 15456864.12565468, 15456864.12565468}
        , new @Nullable Object[]{Double.class, new BigInteger("2516548487843546897665644"),
            2516548487843547000000000d}
        ,
        new @Nullable Object[]{Double.class, new BigDecimal("2516548487843546897665644.541864789"),
            2516548487843547000000000d}
        , new @Nullable Object[]{Double.class, DtDate.of(2015, 4, 12), null}
        , new @Nullable Object[]{Double.class, DtDateTime.of(2022, 12, 5, 10, 24), null}
        , new @Nullable Object[]{Double.class, LocalDate.of(1995, 5, 16), null}
        , new @Nullable Object[]{Double.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5), null}
        , new @Nullable Object[]{Double.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{Double.class, true, null}
        , new @Nullable Object[]{Double.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{Double.class, "Test string", null}
        , new @Nullable Object[]{DtDate.class, (byte) 15, null}
        , new @Nullable Object[]{DtDate.class, (short) 1561, null}
        , new @Nullable Object[]{DtDate.class, 1569852, null}
        , new @Nullable Object[]{DtDate.class, 15474846464646L, null}
        , new @Nullable Object[]{DtDate.class, 15645.786f, null}
        , new @Nullable Object[]{DtDate.class, 15456864.12565468, null}
        , new @Nullable Object[]{DtDate.class, new BigInteger("2516548487843546897665644"), null}
        , new @Nullable Object[]{DtDate.class, new BigDecimal("2516548487843546897665644.541864789"),
            null}
        , new @Nullable Object[]{DtDate.class, DtDate.of(2015, 4, 12), DtDate.of(2015, 4, 12)}
        , new @Nullable Object[]{DtDate.class, DtDateTime.of(2022, 12, 5, 10, 24),
            DtDate.of(2022, 12, 5)}
        , new @Nullable Object[]{DtDate.class, LocalDate.of(1995, 5, 16), DtDate.of(1995, 5, 16)}
        , new @Nullable Object[]{DtDate.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5),
            DtDate.of(2001, 8, 11)}
        , new @Nullable Object[]{DtDate.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{DtDate.class, true, null}
        , new @Nullable Object[]{DtDate.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{DtDate.class, "Test string", null}
        , new @Nullable Object[]{DtDateTime.class, (byte) 15, null}
        , new @Nullable Object[]{DtDateTime.class, (short) 1561, null}
        , new @Nullable Object[]{DtDateTime.class, 1569852, null}
        , new @Nullable Object[]{DtDateTime.class, 15474846464646L, null}
        , new @Nullable Object[]{DtDateTime.class, 15645.786f, null}
        , new @Nullable Object[]{DtDateTime.class, 15456864.12565468, null}
        , new @Nullable Object[]{DtDateTime.class, new BigInteger("2516548487843546897665644"), null}
        , new @Nullable Object[]{DtDateTime.class, new BigDecimal("2516548487843546897665644.541864789"), null}
        , new @Nullable Object[]{DtDateTime.class, DtDate.of(2015, 4, 12), DtDateTime.of(2015, 4, 12)}
        , new @Nullable Object[]{DtDateTime.class, DtDateTime.of(2022, 12, 5, 10, 24),
        DtDateTime.of(2022, 12, 5, 10, 24)}
        , new @Nullable Object[]{DtDateTime.class, LocalDate.of(1995, 5, 16), DtDateTime.of(1995, 5, 16)}
        , new @Nullable Object[]{DtDateTime.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5),
        DtDateTime.of(2001, 8, 11, 12, 25, 5)}
        , new @Nullable Object[]{DtDateTime.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{DtDateTime.class, true, null}
        , new @Nullable Object[]{DtDateTime.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{DtDateTime.class, "Test string", null}
        , new @Nullable Object[]{Integer.class, (byte) 15, 15}
        , new @Nullable Object[]{Integer.class, (short) 1561, 1561}
        , new @Nullable Object[]{Integer.class, 1569852, 1569852}
        , new @Nullable Object[]{Integer.class, 15474846464646L, null}
        , new @Nullable Object[]{Integer.class, 15645.786f, null}
        , new @Nullable Object[]{Integer.class, 15456864.12565468, null}
        , new @Nullable Object[]{Integer.class, new BigInteger("2516548487843546897665644"), null}
        , new @Nullable Object[]{Integer.class, new BigDecimal("2516548487843546897665644.541864789"), null}
        , new @Nullable Object[]{Integer.class, DtDate.of(2015, 4, 12), null}
        , new @Nullable Object[]{Integer.class, DtDateTime.of(2022, 12, 5, 10, 24), null}
        , new @Nullable Object[]{Integer.class, LocalDate.of(1995, 5, 16), null}
        , new @Nullable Object[]{Integer.class, LocalDateTime.of(2001, 8, 11, 12, 25, 5), null}
        , new @Nullable Object[]{Integer.class, DtUid.valueOf("10002321445985"), null}
        , new @Nullable Object[]{Integer.class, true, null}
        , new @Nullable Object[]{Integer.class, DbBoolean.FALSE, null}
        , new @Nullable Object[]{Integer.class, "Test string", null}
    );
  }

  @ParameterizedTest
  @MethodSource
  void convertTest(Class<?> targetType, Object value, @Nullable Object result) {
    if (result == null) {
      assertThatThrownBy(() -> TypeMapImpl.getDefault().convert(targetType, value));
    } else {
      assertThat(TypeMapImpl.getDefault().convert(targetType, value))
          .isEqualTo(result);
    }
  }
}