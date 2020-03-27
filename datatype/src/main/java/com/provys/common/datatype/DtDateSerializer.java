package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;

/**
 * Class is Jackson serializer for {@link DtDate} class.
 */
@SuppressWarnings("CyclicClassDependency") // cyclic dependency with adapters is to be expected
public class DtDateSerializer extends StdScalarSerializer<DtDate> {

  private static final long serialVersionUID = 4486955492994705194L;

  protected DtDateSerializer() {
    super(DtDate.class);
  }

  @Override
  public void serialize(DtDate value, JsonGenerator generator,
      SerializerProvider serializerProvider)
      throws IOException {
    generator.writeString(value.toIso());
  }
}
