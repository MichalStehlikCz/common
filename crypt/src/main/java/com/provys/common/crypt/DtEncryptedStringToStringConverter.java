package com.provys.common.crypt;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Converter used for Jackson deserialization of {@link DtEncryptedString}.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its converter is ok
final class DtEncryptedStringToStringConverter extends StdConverter<DtEncryptedString, String> {

  @Override
  public String convert(DtEncryptedString dtEncryptedString) {
    return dtEncryptedString.getIisValue();
  }
}
