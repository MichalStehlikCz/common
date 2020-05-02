package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import org.checkerframework.framework.qual.Covariant;

/**
 * Converter that is used to convert values of source type to target type.
 *
 * @param <S> is source type
 * @param <T> is target type
 */
@Immutable
@Covariant(0)
public interface TypeConverter<S, T> extends Serializable {

  /**
   * Retrieve source type, supported by this converter.
   *
   * @return source type, supported by this converter
   */
  Class<S> getSourceType();

  /**
   * Retrieve target type, supported by this converter.
   *
   * @return target type, supported by this converter
   */
  Class<T> getTargetType();

  /**
   * Priority of this converter. If there are multiple converters for same combination of source and
   * target type, converter with highest priority is used.
   *
   * @return priority of this converter
   */
  default int getPriority() {
    return 0;
  }

  /**
   * Indicates if source should be considered "subtype" of target. Means any value of source type
   * can be converted to target type and there is no loss of information (target type has at least
   * same precision as source). For example, it is true if source is Float and target Double, but
   * not the other way around.
   *
   * @return is source is subset of values of target
   */
  boolean isAssignableFrom();

  /**
   * Convert value of source type to target type.
   *
   * @param value is source value
   * @return value of target type, corresponding to source value. Conversion is performed only if
   *     source value can be represented in target type
   */
  T convert(S value);
}
