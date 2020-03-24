package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Jackson serializer for {@link DtTimeS} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtTimeSSerializer extends JsonSerializer<DtTimeS> {

  @Override
  public void serialize(DtTimeS value, JsonGenerator generator,
      SerializerProvider serializerProvider)
      throws IOException {
    generator.writeString(value.toIso());
  }
}
