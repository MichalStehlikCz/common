package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

/**
 * Jackson deserializer for {@link DtTimeS} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtTimeSDeserializer extends StdScalarDeserializer<DtTimeS> {

  private static final long serialVersionUID = 831768558113799559L;

  protected DtTimeSDeserializer() {
    super(DtTimeS.class);
  }

  @Override
  public DtTimeS deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    return DtTimeS.parseIso(parser.getValueAsString());
  }
}
