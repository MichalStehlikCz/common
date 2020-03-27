package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;

/**
 * Jackson serializer for {@link DtDateTime} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtDateTimeSerializer extends StdScalarSerializer<DtDateTime> {

  private static final long serialVersionUID = 7815513217551522235L;

  protected DtDateTimeSerializer() {
    super(DtDateTime.class);
  }

  @Override
  public void serialize(DtDateTime value, JsonGenerator generator,
      SerializerProvider serializerProvider) throws IOException {
    generator.writeString(value.toIso());
  }
}
