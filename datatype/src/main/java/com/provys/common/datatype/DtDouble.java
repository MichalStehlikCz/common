package com.provys.common.datatype;

/**
 * Implements support for Provys domain NUMBER.
 */
public final class DtDouble {

  /**
   * Missing privileges indicator for Provys type NUMBER.
   */
  public static final Double PRIV = -2135412459d;
  /**
   * Multi-value indicator for Provys type NUMBER.
   */
  public static final Double ME = -2135412458d;
  /**
   * Minimal valid Provys NUMBER value.
   */
  public static final Double MIN = -2135412457d;
  /**
   * Maximal valid Provys NUMBER value.
   */
  public static final Double MAX = (double) Integer.MAX_VALUE;

  /**
   * Indicates if supplied value is valid (e.g. regular or special value).
   *
   * @param value is the value to be checked
   * @return true if supplied value is regular value from within MIN..MAX interval or special value,
   *     false otherwise
   */
  public static boolean isValid(double value) {
    return ((value >= MIN) && (value <= MAX)) || (value == PRIV) || (value == ME);
  }

  /**
   * Indicates if supplied value is regular (e.g. not one of special values, inside interval of MIN
   * .. MAX).
   *
   * @param value is the value to be checked
   * @return true if supplied value is regular value from within MIN..MAX interval, false otherwise
   */
  public static boolean isRegular(double value) {
    return (value > MIN) && (value < MAX);
  }

  /**
   * Indicates if supplied value can be valid value held in NUMBER property. Note that most integer
   * properties can only hold regular values, nevertheless there might be some situations
   * (especially when property indicates lower or upper bound) when even MIN and MAX values are
   * valid.
   *
   * @param value is the value to be checked
   * @return true if supplied value is from (inclusive) MIN .. MAX interval, false otherwise
   */
  public static boolean isValidValue(double value) {
    return (value >= MIN) && (value <= MAX);
  }

  /**
   * Indicates if supplied value is special value indicating missing privileges.
   *
   * @param value is the value to be checked
   * @return true if it is PRIV value, false otherwise
   */
  public static boolean isPriv(double value) {
    return value == PRIV;
  }

  /**
   * Indicates if supplied value is special value indicating multi-value.
   *
   * @param value is the value to be checked
   * @return true if it is ME value, false otherwise
   */
  public static boolean isME(double value) {
    return value == ME;
  }

  /**
   * Indicates if supplied value is minimal value.
   *
   * @param value is the value to be checked
   * @return true if it is MIN value, false otherwise
   */
  public static boolean isMin(double value) {
    return value == MIN;
  }

  /**
   * Indicates if supplied value is maximal value.
   *
   * @param value is the value to be checked
   * @return true if it is MAX value, false otherwise
   */
  public static boolean isMax(double value) {
    return value == MAX;
  }

  /**
   * DtDouble is utility class used for manipulation with double values in Provys framework and this
   * is why its constructor is not accessible.
   */
  private DtDouble() {
  }
}
