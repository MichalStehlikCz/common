package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;

/**
 * Maps strings used to identify class in Json / Xml serialisation to Java classes and vica versa.
 */
@Immutable
public interface TypeMap extends Serializable {

  /**
   * Retrieve class, corresponding to supplied name.
   *
   * @param name is name associated with class
   * @return class represented by given name
   */
  Class<?> getType(String name);

  /**
   * Retrieve name representing supplied class.
   *
   * @param type is class we want to retrieve name of
   * @return name representing given class
   */
  String getName(Class<?> type);
}
