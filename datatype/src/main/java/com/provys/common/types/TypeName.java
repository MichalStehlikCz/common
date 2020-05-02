package com.provys.common.types;

import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.annotations.ImmutableTypeParameter;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Allows to specify required mapping of given class to name used in serialization. Used for module
 * registration.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its serialization proxy
@Immutable
public final class TypeName<@ImmutableTypeParameter T extends Serializable>
    implements Serializable {

  private final Class<T> type;
  private final String name;
  private final boolean defaultForType;

  /**
   * Create new type name.
   *
   * @param type           is type (class) name is used for
   * @param name           is name of the type
   * @param defaultForType denotes if given name is default name for this type. There might be
   *                       multiple non-default names for given type, but only one default one
   */
  public TypeName(Class<T> type, String name, boolean defaultForType) {
    this.type = type;
    this.name = name;
    this.defaultForType = defaultForType;
  }

  /**
   * Create new default type name.
   *
   * @param type is type (class) name is used for
   * @param name is name of the type
   */
  public TypeName(Class<T> type, String name) {
    this(type, name, true);
  }

  /**
   * Value of field type.
   *
   * @return value of field type
   */
  public Class<T> getType() {
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

  /**
   * Value of field defaultForType.
   *
   * @return value of field defaultForType
   */
  public boolean isDefaultForType() {
    return defaultForType;
  }

  /**
   * Supports serialization via SerializationProxy.
   *
   * @return proxy, corresponding to this TypeName
   */
  private Object writeReplace() {
    return new SerializationProxy(this);
  }

  /**
   * Should be serialized via proxy, thus no direct deserialization should occur.
   *
   * @param stream is stream from which object is to be read
   * @throws InvalidObjectException always
   */
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Use Serialization Proxy instead.");
  }

  private static final class SerializationProxy implements Serializable {

    private static final long serialVersionUID = 8997597081930986114L;
    private @Nullable Class<? extends Serializable> type;
    private @Nullable String name;
    private @Nullable Boolean defaultForType;

    SerializationProxy() {
    }

    <T extends Serializable> SerializationProxy(TypeName<T> value) {
      this.type = value.type;
      this.name = value.name;
      this.defaultForType = value.defaultForType;
    }

    @SuppressWarnings("Immutable")
    // no way to verify immutability at runtime without adding runtime dependency on error-prone,
    // which is not good idea
    private Object readResolve() throws InvalidObjectException {
      if (type == null) {
        throw new InvalidObjectException("Type not read during TypeName deserialization");
      }
      if (name == null) {
        throw new InvalidObjectException("Name not read during TypeName deserialization");
      }
      if (defaultForType == null) {
        throw new InvalidObjectException("DefaultForType not read during TypeName deserialization");
      }
      return new TypeName<>(type, name, defaultForType);
    }
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeName<?> typeName = (TypeName<?>) o;
    return (type == typeName.type)
        && name.equals(typeName.name)
        && defaultForType == typeName.defaultForType;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (defaultForType ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TypeName{"
        + "type=" + type
        + ", name='" + name + '\''
        + ", defaultForType=" + defaultForType
        + '}';
  }
}
