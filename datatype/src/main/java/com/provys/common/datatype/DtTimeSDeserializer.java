package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Jackson deserializer for {@link DtTimeS} class
 */
public class DtTimeSDeserializer extends JsonDeserializer<DtTimeS> {
    @Override
    public DtTimeS deserialize(JsonParser parser, DeserializationContext deserializationContext)
            throws IOException {
        return DtTimeS.parseIso(parser.getValueAsString());
    }
}
