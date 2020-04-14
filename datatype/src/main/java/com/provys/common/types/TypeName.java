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
@Immutable
public final class TypeName<@ImmutableTypeParameter T extends Serializable>
    implements Serializable {

  private final Class<T> type;
  private final String name;

  public TypeName(Class<T> type, String name) {
    this.type = type;
    this.name = name;
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

    SerializationProxy() {
    }

    <T extends Serializable> SerializationProxy(TypeName<T> value) {
      this.type = value.type;
      this.name = value.name;
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
      return new TypeName<>(type, name);
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
