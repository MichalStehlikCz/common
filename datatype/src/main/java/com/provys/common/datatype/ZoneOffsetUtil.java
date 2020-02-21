package com.provys.common.datatype;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

import static org.checkerframework.checker.nullness.NullnessUtil.castNonNull;

/**
 * Utility class that provides methods missing in Java ZoneOffset implementation (like validation
 * without raising exception).
 */
public final class ZoneOffsetUtil {

  /**
   * Regular expression for zone offset part of xsd time information. Strict form (either Z for GMT,
   * [+-]HH:MI and [+-]HH)
   */
  public static final String REGEX_STRICT = "Z|[+-](?:0[0-9]|1[0-4]):[0-5][0-9]|[+-](?:0?[0-9]|1[0-4])";

  /**
   * Pattern for strict zone offset validation.
   */
  public static final Pattern PATTERN_STRICT = Pattern.compile(REGEX_STRICT);

  /**
   * Regular expression for offset part of time information. Lenient form allows offset range -18 to
   * +18 hours and supports same formats as ZoneOffset (in addition to strict formats z for GMT,
   * [+-]HHMI, [+-]HH:MI:SS and [+-]HHMISS
   */
  public static final String REGEX_LENIENT =
      "[zZ]|(?:[+-](?:0[0-9]|1[0-8]):[0-5][0-9](?::[0-5][0-9])?)|" +
          "[+-](?:0?[0-9]|1[0-8])|" +
          "[+-](?:(?:0[0-9]|1[0-8])[0-5][0-9](?:[0-5][0-9])?)";

  /**
   * Pattern for lenient zone offset validation.
   */
  public static final Pattern PATTERN_LENIENT = Pattern.compile(REGEX_LENIENT);

  /**
   * Strict validation of zone offset text.
   *
   * @param text is source text
   * @return if supplied text is valid zone offset (strict validation)
   */
  public static boolean isValidIsoStrict(String text) {
    return PATTERN_STRICT.matcher(text).matches();
  }

  /**
   * Strict validation of zone offset text.
   *
   * @param text is source text
   * @return if supplied text is valid zone offset (strict validation)
   */
  public static boolean isValidIsoLenient(String text) {
    return PATTERN_LENIENT.matcher(text).matches();
  }

  /**
   * Parsing of zone offset text. Should accept all values that pass at least lenient validation
   *
   * @param text is text to be parsed
   * @return parsed zone offset
   */
  public static ZoneOffset parseIso(String text) {
    try {
      if (text.equals("z")) {
        return ZoneOffset.UTC;
      }
      return ZoneOffset.of(text);
    } catch (DateTimeException e) {
      throw new DateTimeParseException(castNonNull(e.getMessage()), text, 0, e);
    }
  }

  /**
   * Non-instantiable utility class.
   */
  private ZoneOffsetUtil() {
  }
}
