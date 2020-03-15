package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Jackson deserializer for {@link DtDate} class.
 */
public class DtDateDeserializer extends JsonDeserializer<DtDate> {

  @Override
  public DtDate deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    return DtDate.parseIso(parser.getValueAsString());
  }
}
