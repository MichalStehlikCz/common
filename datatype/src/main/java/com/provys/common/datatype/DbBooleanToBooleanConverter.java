package com.provys.common.datatype;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Converter used for Jackson serialization of {@link DbBoolean}.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its converter is ok
class DbBooleanToBooleanConverter extends StdConverter<DbBoolean, Boolean> {

  @Override
  public Boolean convert(DbBoolean dbBoolean) {
    return dbBoolean.value();
  }
}
