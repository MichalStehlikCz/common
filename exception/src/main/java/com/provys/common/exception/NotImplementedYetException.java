package com.provys.common.exception;

/**
 * Exception to be thrown in places where method has been created to comply with interface
 * declaration, but has not been implemented yet. Unlike {@link NotImplementedException} that
 * means method should not have been called, this exception means method can be called and
 * implementation should be added to fix this exception.
 */
public final class NotImplementedYetException extends ProvysException {

  private static final String NAME_NM = "JAVA_NOT_IMPLEMENTED_YET";

  public NotImplementedYetException(Class<?> clazz) {
    super("Method waiting for implementation in class " + clazz.getCanonicalName());
  }

  @Override
  public String getNameNm() {
    return NAME_NM;
  }
}
