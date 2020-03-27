package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

/**
 * Jackson deserializer for {@link DtDateTime} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtDateTimeDeserializer extends StdScalarDeserializer<DtDateTime> {

  private static final long serialVersionUID = -2802732968216464500L;

  protected DtDateTimeDeserializer() {
    super(DtDateTime.class);
  }

  @Override
  public DtDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext)
      throws IOException {
    return DtDateTime.parse(parser.getValueAsString());
  }
}
