package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class DtTimeSDeserializer extends JsonDeserializer<DtTimeS> {
    @Override
    public DtTimeS deserialize(JsonParser parser, DeserializationContext deserializationContext)
            throws IOException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        return DtTimeS.parseIso(parser.getValueAsString());
    }
}
