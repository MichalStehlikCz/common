package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Jackson deserializer for DtUid class
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DtUidDeserializer extends JsonDeserializer<DtUid> {
    @Override
    public DtUid deserialize(JsonParser parser, DeserializationContext deserializationContext)
            throws IOException {
        return DtUid.valueOf(parser.getBigIntegerValue());
    }
}

