package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;

/**
 * Jackson serializer for {@link DtUid} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtUidSerializer extends StdScalarSerializer<DtUid> {

  private static final long serialVersionUID = 7330448786647473578L;

  protected DtUidSerializer() {
    super(DtUid.class);
  }

  @Override
  public void serialize(DtUid value, JsonGenerator generator, SerializerProvider serializerProvider)
      throws IOException {
    generator.writeNumber(value.getValue());
  }
}
