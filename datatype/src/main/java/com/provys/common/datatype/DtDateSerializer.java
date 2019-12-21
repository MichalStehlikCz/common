package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Class is Jackson serializer for DtDate class
 */
public class DtDateSerializer extends JsonSerializer<DtDate> {

    @Override
    public void serialize(DtDate value, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException {
        generator.writeString(value.toIso());
    }
}
