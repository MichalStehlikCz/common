package com.provys.common.crypt;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Converter used for Jackson deserialization of {@link DtEncryptedString}.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its converter is ok
final class DtEncryptedStringFromStringConverter extends StdConverter<String, DtEncryptedString> {

  @Override
  public DtEncryptedString convert(String value) {
    return DtEncryptedString.valueOf(value);
  }
}
