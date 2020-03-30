package com.provys.common.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Json serializer, using type name as available in Sql type map.
 */
public final class ProvysClassSerializer extends StdScalarSerializer<Class<?>> {

  private final TypeMap typeMap;

  /**
   * Create serializer using supplied map.
   *
   * @param typeMap is type map used for translation of object type to name
   */
  @SuppressWarnings("unchecked")
  public ProvysClassSerializer(TypeMap typeMap) {
    super((Class<Class<?>>)(Class<?>) Class.class);
    this.typeMap = typeMap;
  }

  /**
   * Create serializer using default map.
   */
  public ProvysClassSerializer() {
    this(TypeMapImpl.getDefault());
  }

  @Override
  public void serialize(Class<?> type, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(typeMap.getName(type));
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

    private static final long serialVersionUID = 4804041908873807840L;
    private @Nullable TypeMap typeMap;

    SerializationProxy() {
    }

    SerializationProxy(ProvysClassSerializer value) {
      this.typeMap =
          value.typeMap.equals(TypeMapImpl.getDefault()) ? null : value.typeMap;
    }

    private Object readResolve() {
      return (typeMap == null) ? new ProvysClassSerializer()
          : new ProvysClassSerializer(typeMap);
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
    ProvysClassSerializer that = (ProvysClassSerializer) o;
    return typeMap.equals(that.typeMap);
  }

  @Override
  public int hashCode() {
    return typeMap.hashCode();
  }

  @Override
  public String toString() {
    return "DefaultJsonClassSerializer{"
        + "typeMap=" + typeMap + '}';
  }
}
