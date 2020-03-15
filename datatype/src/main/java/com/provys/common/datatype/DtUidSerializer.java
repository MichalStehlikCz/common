package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Jackson serializer for {@link DtUid} class.
 */
public class DtUidSerializer extends JsonSerializer<DtUid> {

  @Override
  public void serialize(DtUid value, JsonGenerator generator, SerializerProvider serializerProvider)
      throws IOException {
    generator.writeNumber(value.getValue());
  }
}
