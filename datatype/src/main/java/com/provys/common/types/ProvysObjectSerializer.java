package com.provys.common.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.errorprone.annotations.Immutable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Json serializer, using type name as available in Sql type map.
 */
@SuppressWarnings("CyclicClassDependency") // dependency between class and its serialization proxy
@Immutable
public final class ProvysObjectSerializer extends StdSerializer<Serializable> {

  private final TypeMap typeMap;

  /**
   * Create serializer using supplied map.
   *
   * @param typeMap is type map used for translation of object type to name
   */
  public ProvysObjectSerializer(TypeMap typeMap) {
    super(Serializable.class);
    this.typeMap = typeMap;
  }

  /**
   * Create serializer using default map.
   */
  public ProvysObjectSerializer() {
    this(TypeMapImpl.getDefault());
  }

  /**
   * Serialize object as field value (e.g. do not include object around field name and value).
   *
   * @param value is object to be serialized
   * @param generator is Jackson Json generator where value should be written
   * @throws IOException when IO exception is encountered accessing Json generator
   */
  public void serializeField(Object value, JsonGenerator generator) throws IOException {
    generator.writeObjectField(typeMap.getName(value.getClass().asSubclass(Serializable.class)),
        value);
  }

  @Override
  public void serialize(Serializable value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeStartObject();
    serializeField(value, generator);
    generator.writeEndObject();
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

    private static final long serialVersionUID = 2842443821992021959L;
    private @Nullable TypeMap typeMap;

    SerializationProxy() {
    }

    SerializationProxy(ProvysObjectSerializer value) {
      this.typeMap =
          value.typeMap.equals(TypeMapImpl.getDefault()) ? null : value.typeMap;
    }

    private Object readResolve() {
      return (typeMap == null) ? new ProvysObjectSerializer()
          : new ProvysObjectSerializer(typeMap);
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
    ProvysObjectSerializer that = (ProvysObjectSerializer) o;
    return typeMap.equals(that.typeMap);
  }

  @Override
  public int hashCode() {
    return typeMap.hashCode();
  }

  @Override
  public String toString() {
    return "ProvysObjectSerializer{"
        + "typeMap=" + typeMap + '}';
  }
}
