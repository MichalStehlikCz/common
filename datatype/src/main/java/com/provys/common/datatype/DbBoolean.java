package com.provys.common.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class is used to represent boolean expressions from database (e.g. PL/SQL BOOLEAN data type).
 * "Normal" Java Boolean class is mapped to database CHAR(1 BYTE) with values Y / N (conversion
 * functions are in {@code DtBoolean}) and it would cause problems if Boolean type was bound to both
 * of these types, as it would be syntactically correct to for example compare values 'Y' and
 * TRUE... Given that BOOLEAN is only available in PL/SQL, not in SQL, it is rarely used and thus it
 * seems ok to use DbBoolean wrapper in such situations.
 */
@JsonSerialize(converter = DbBooleanToBooleanConverter.class)
@JsonDeserialize(converter = DbBooleanFromBooleanConverter.class)
public enum DbBoolean {

  TRUE(true),
  FALSE(false);

  /**
   * Database boolean value, representing given Java boolean value.
   *
   * @param value is value to be represented
   * @return corresponding database boolean value
   */
  public static DbBoolean of(boolean value) {
    return value ? TRUE : FALSE;
  }

  private final boolean value;

  DbBoolean(boolean value) {
    this.value = value;
  }

  /**
   * Boolean value corresponding to this database boolean.
   *
   * @return boolean value corresponding to this database boolean
   */
  public boolean value() {
    return value;
  }
}