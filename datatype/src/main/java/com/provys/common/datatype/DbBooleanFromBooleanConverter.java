package com.provys.common.datatype;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Converter used for Jackson deserialization of {@link DbBoolean}.
 */
class DbBooleanFromBooleanConverter extends StdConverter<Boolean, DbBoolean> {

  @Override
  public DbBoolean convert(Boolean value) {
    return DbBoolean.of(value);
  }
}
