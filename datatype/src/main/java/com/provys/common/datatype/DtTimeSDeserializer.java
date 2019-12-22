package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DtTimeSDeserializer extends JsonDeserializer<DtTimeS> {
    @Override
    public DtTimeS deserialize(JsonParser parser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        return DtTimeS.parseIso(parser.getValueAsString());
    }
}
