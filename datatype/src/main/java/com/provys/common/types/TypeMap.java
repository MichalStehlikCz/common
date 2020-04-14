package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;

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
