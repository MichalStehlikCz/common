package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.PolyNull;

/**
 * Maps strings used to identify class in Json / Xml serialisation to Java classes and vica versa.
 */
@Immutable
public interface TypeMap extends Serializable {

  /**
   * Retrieve class, corresponding to supplied name. Without irregular mapping used for object
   * class. Return type should always be Serializable and immutable.
   *
   * @param name is name associated with class
   * @return class represented by given name
   */
  Class<? extends Serializable> getType(String name);

  /**
   * Retrieve class, corresponding to supplied name, including Object.class mapping.
   *
   * @param name is name associated with class
   * @return class represented by given name
   */
  Class<?> getExtendedType(String name);

  /**
   * Validate that type corresponds to one of supported (serialized, immutable) value types.
   *
   * @param type is type to be validated
   */
  void validateType(Class<?> type);

  /**
   * Returns true if target type can accept value of source type (implicit conversion exists). In
   * general, implicit conversions are allowed from type with less values or smaller precision
   * to type with more values / higher precision, but not the other way around. Target type must be
   * one of supported types, source types might be wider range
   *
   * @param targetType is target type for conversion
   * @param sourceType is source type for conversion
   * @return true if such implicit conversion is possible and false otherwise
   */
  boolean isAssignableFrom(Class<?> targetType, Class<?> sourceType);

  /**
   * Convert value to target type using implicit conversion, throw exception if implicit conversion
   * is not possible. Conversion should always be possible with assignable type, but might also be
   * possible in other situations; range checking is performed in such situation
   *
   * @param targetType is target type for conversion
   * @param value is source value
   * @param <T> is type parameter corresponding to target type
   * @return converted value
   */
  <T> @PolyNull T convert(Class<T> targetType, @PolyNull Object value);

  /**
   * Retrieve name representing supplied class. Without irregular mapping, used for Object.class.
   *
   * @param type is class we want to retrieve name of
   * @return name representing given class
   */
  String getName(Class<?> type);

  /**
   * Retrieve name representing supplied class, including mapping of Object.class.
   *
   * @param type is class we want to retrieve name of
   * @return name representing given class
   */
  String getExtendedName(Class<?> type);

  /**
   * Retrieve type, corresponding to any value.
   *
   * @return Object.class as type, corresponding to any value
   */
  Class<Object> getAnyType();

  /**
   * Retrieve name, representing any value.
   *
   * @return name, representing any value (Object.class)
   */
  String getAnyName();
}
