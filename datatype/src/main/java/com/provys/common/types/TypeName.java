package com.provys.common.types;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Allows to specify required mapping of given class to name used in serialization. Used for module
 * registration.
 */
public final class TypeName {

  private final Class<?> type;
  private final String name;

  public TypeName(Class<?> type, String name) {
    this.type = type;
    this.name = name;
  }

  /**
   * Value of field type.
   *
   * @return value of field type
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Value of field name.
   *
   * @return value of field name
   */
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeName typeName = (TypeName) o;
    return (type == typeName.type)
        && name.equals(typeName.name);
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "TypeName{"
        + "type=" + type
        + ", name='" + name + '\''
        + '}';
  }
}
