package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

/**
 * Jackson deserializer for {@link DtDate} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtDateDeserializer extends StdScalarDeserializer<DtDate> {

  private static final long serialVersionUID = 416004834651048439L;

  protected DtDateDeserializer() {
    super(DtDate.class);
  }

  @Override
  public DtDate deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    return DtDate.parseIso(parser.getValueAsString());
  }
}
