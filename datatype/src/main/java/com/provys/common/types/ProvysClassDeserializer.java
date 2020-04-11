package com.provys.common.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Deserializer that translates Provys string (not Java specific) to class.
 */
public final class ProvysClassDeserializer extends StdScalarDeserializer<Class<?>> {

  private final TypeMap typeMap;

  /**
   * Create serializer using supplied map.
   *
   * @param typeMap is type map used for translation of object type to name
   */
  public ProvysClassDeserializer(TypeMap typeMap) {
    super(Class.class);
    this.typeMap = typeMap;
  }

  /**
   * Create serializer using default map.
   */
  public ProvysClassDeserializer() {
    this(TypeMapImpl.getDefault());
  }

  @Override
  public Class<?> deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    return typeMap.getType(parser.getValueAsString());
  }

  /**
   * Supports serialization via SerializationProxy.
   *
   * @return proxy, corresponding to this DefaultJsonClassDeserializer
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

    private static final long serialVersionUID = 3418457153760188855L;
    private @Nullable TypeMap typeMap;

    SerializationProxy() {
    }

    SerializationProxy(ProvysClassDeserializer value) {
      this.typeMap =
          value.typeMap.equals(TypeMapImpl.getDefault()) ? null : value.typeMap;
    }

    private Object readResolve() {
      return (typeMap == null) ? new ProvysClassDeserializer()
          : new ProvysClassDeserializer(typeMap);
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
    ProvysClassDeserializer that = (ProvysClassDeserializer) o;
    return typeMap.equals(that.typeMap);
  }

  @Override
  public int hashCode() {
    return typeMap.hashCode();
  }

  @Override
  public String toString() {
    return "DefaultJsonClassDeserializer{"
        + "typeMap=" + typeMap + '}';
  }
}
