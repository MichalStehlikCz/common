package com.provys.common.exception;

/**
 * Exception to be thrown in places where some functions is intentionally not implemented. This can
 * be either in test class where it is not expected to be used or in class that does not implement
 * some feature, because this feature makes no sense for given class.
 */
public final class NotImplementedException extends ProvysException {

  private static final String NAME_NM = "JAVA_NOT_IMPLEMENTED";

  public NotImplementedException(Class<?> clazz) {
    super("Method not implemented in class " + clazz);
  }

  @Override
  public String getNameNm() {
    return NAME_NM;
  }

  @Override
  public String toString() {
    return "NotImplementedException{" + super.toString() + '}';
  }
}
