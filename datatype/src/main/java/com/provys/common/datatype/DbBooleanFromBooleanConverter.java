package com.provys.common.datatype;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Converter used for Jackson deserialization of {@link DbBoolean}.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its converter is ok
class DbBooleanFromBooleanConverter extends StdConverter<Boolean, DbBoolean> {

  @Override
  public DbBoolean convert(Boolean value) {
    return DbBoolean.of(value);
  }
}
