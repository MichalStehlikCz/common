package com.provys.common.types;

import java.math.BigInteger;

/**
 * Utility class, with functions used for standard conversions.
 */
final class TypeConversionUtil {

  private TypeConversionUtil() {
  }

  static int floatToIntExact(float value) {
    if (value != Math.round(value)) {
      throw new ArithmeticException("Integer conversion failed - float value has fractional part");
    }
    return Math.toIntExact(Math.round(value));
  }

  static int doubleToIntExact(double value) {
    if (value != Math.round(value)) {
      throw new ArithmeticException("Integer conversion failed - double value has fractional part");
    }
    return Math.toIntExact(Math.round(value));
  }

  static BigInteger floatToBigIntegerExact(float value) {
    if (value != Math.round(value)) {
      throw new ArithmeticException(
          "BigInteger conversion failed - float value has fractional part");
    }
    return BigInteger.valueOf(Math.round(value));
  }

  static BigInteger doubleToBigIntegerExact(double value) {
    if (value != Math.round(value)) {
      throw new ArithmeticException(
          "BigInteger conversion failed - double value has fractional part");
    }
    return BigInteger.valueOf(Math.round(value));
  }
}
