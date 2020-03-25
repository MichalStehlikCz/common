package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

/**
 * Jackson deserializer for {@link DtUid} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtUidDeserializer extends StdScalarDeserializer<DtUid> {

  protected DtUidDeserializer() {
    super(DtUid.class);
  }

  @Override
  public DtUid deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    if (parser.getCurrentToken().isNumeric()) {
      return DtUid.valueOf(parser.getBigIntegerValue());
    }
    return DtUid.valueOf(parser.getValueAsString());
  }
}

