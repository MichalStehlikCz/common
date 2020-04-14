package com.provys.common.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Deserializer for deserialization of Json objects of unknown type, using class name translated to
 * type via type map. Formally created as Serializable deserializer, as type map only supports
 * Serializable object types.
 */
public final class ProvysObjectDeserializer extends StdDeserializer<Serializable> {

  private final TypeMap typeMap;

  /**
   * Create deserializer using specified type map.
   *
   * @param typeMap is type map that will be used to look up class based on name
   */
  public ProvysObjectDeserializer(TypeMap typeMap) {
    super(Serializable.class);
    this.typeMap = typeMap;
  }

  /**
   * Create deserializer using default type map.
   */
  public ProvysObjectDeserializer() {
    this(TypeMapImpl.getDefault());
  }

  @Override
  public Serializable deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    if (!parser.isExpectedStartObjectToken()) {
      throw context.wrongTokenException(parser, Serializable.class, JsonToken.START_OBJECT,
          "start");
    }
    var typeName = parser.nextFieldName();
    if (typeName == null) {
      throw context.wrongTokenException(parser, Serializable.class, JsonToken.FIELD_NAME,
          "type name");
    }
    var type = typeMap.getType(typeName).asSubclass(Serializable.class);
    parser.nextToken();
    var result = context.readValue(parser, type);
    if (result == null) {
      context.reportInputMismatch(Serializable.class, "null value encountered");
    }
    if (!parser.nextToken().isStructEnd()) {
      throw context.wrongTokenException(parser, Serializable.class, JsonToken.END_OBJECT,
          "end");
    }
    return result;
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

    private static final long serialVersionUID = 5585642547380887781L;
    private @Nullable TypeMap typeMap;

    SerializationProxy() {
    }

    SerializationProxy(ProvysObjectDeserializer value) {
      this.typeMap =
          value.typeMap.equals(TypeMapImpl.getDefault()) ? null : value.typeMap;
    }

    private Object readResolve() {
      return (typeMap == null) ? new ProvysObjectDeserializer()
          : new ProvysObjectDeserializer(typeMap);
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
    ProvysObjectDeserializer that = (ProvysObjectDeserializer) o;
    return typeMap.equals(that.typeMap);
  }

  @Override
  public int hashCode() {
    return typeMap.hashCode();
  }

  @Override
  public String toString() {
    return "ProvysObjectDeserializer{"
        + "typeMap=" + typeMap + '}';
  }
}
