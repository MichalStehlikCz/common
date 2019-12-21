package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Jackson deserializer for DtDate class
 */
public class DtDateDeserializer extends JsonDeserializer<DtDate> {

    @Override
    public DtDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        return DtDate.parseIso(parser.getValueAsString());
    }
}
