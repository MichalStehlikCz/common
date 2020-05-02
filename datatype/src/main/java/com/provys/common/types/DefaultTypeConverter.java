package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Type converter that can convert values of type S to type T using function (potentially lambda
 * expression). Note that equality might be tricky when using lambdas, as conversion function is
 * part of equality comparison, but behaviour of comparison on captured lambdas is tricky.
 *
 * @param <S> is source type
 * @param <T> is target type
 */
@Immutable
public final class DefaultTypeConverter<S, T> implements TypeConverter<S, T> {

  private static final long serialVersionUID = -1773444847633019781L;

  private final Class<S> sourceType;
  private final Class<T> targetType;
  private final int priority;
  private final boolean assignableFrom;
  private final SerializableFunction<? super S, ? extends T> conversionFunction;

  /**
   * Constructor that creates default converter with supplied behaviour.
   *
   * @param sourceType         is source type (type of value being converted)
   * @param targetType         is target type (type value is converted to)
   * @param priority           is priority of converter; can be used to replace standard converter
   *                           with custom one
   * @param assignableFrom     defines, if conversion is possible for all values and does not lead
   *                           to loss of precision, meaning it should be used implicitly
   * @param conversionFunction is function that actually performs the conversion
   */
  public DefaultTypeConverter(Class<S> sourceType, Class<T> targetType, int priority,
      boolean assignableFrom, SerializableFunction<? super S, ? extends T> conversionFunction) {
    this.sourceType = sourceType;
    this.targetType = targetType;
    this.priority = priority;
    this.assignableFrom = assignableFrom;
    this.conversionFunction = conversionFunction;
  }

  /**
   * Constructor that produces converter with default priority (0).
   *
   * @param sourceType         is source type (type of value being converted)
   * @param targetType         is target type (type value is converted to)
   * @param assignableFrom     defines, if conversion is possible for all values and does not lead
   *                           to loss of precision, meaning it should be used implicitly
   * @param conversionFunction is function that actually performs the conversion
   */
  public DefaultTypeConverter(Class<S> sourceType, Class<T> targetType,
      boolean assignableFrom, SerializableFunction<? super S, ? extends T> conversionFunction) {
    this(sourceType, targetType, 0, assignableFrom, conversionFunction);
  }

  @Override
  public Class<S> getSourceType() {
    return sourceType;
  }

  @Override
  public Class<T> getTargetType() {
    return targetType;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public boolean isAssignableFrom() {
    return assignableFrom;
  }

  @Override
  public T convert(S value) {
    return conversionFunction.apply(value);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultTypeConverter<?, ?> that = (DefaultTypeConverter<?, ?>) o;
    return priority == that.priority
        && assignableFrom == that.assignableFrom
        && (sourceType == that.sourceType)
        && (targetType == that.targetType)
        && conversionFunction.equals(that.conversionFunction);
  }

  @Override
  public int hashCode() {
    int result = sourceType.hashCode();
    result = 31 * result + targetType.hashCode();
    result = 31 * result + priority;
    result = 31 * result + (assignableFrom ? 1 : 0);
    // conversion function is intentionally excluded from hashcode
    return result;
  }

  @Override
  public String toString() {
    return "DefaultTypeConverter{"
        + "sourceType=" + sourceType
        + ", targetType=" + targetType
        + ", priority=" + priority
        + ", assignableFrom=" + assignableFrom
        // conversion function is intentionally excluded from toString
        + '}';
  }
}
