package com.provys.common.datatype;

import javax.json.bind.adapter.JsonbAdapter;
import java.time.format.DateTimeParseException;

/**
 * Adapter ensuring provys integer value is treated as plain numeric value when
 * serializing to / deserializing from Json using JSON-B
 *
 * @author stehlik
 */
public class JsonbDtDateAdapter implements JsonbAdapter<DtDate, String> {

    /**
     * Unwrap value to ensure it is properly serialized to Json
     *
     * @param original is source provys datatype value
     * @return unwrapped value (String)
     */
    @Override
    public String adaptToJson(DtDate original) {
        if (original.isPriv()) {
            return "1000-01-02";
        }
        if (original.isME()) {
            return "1000-01-01";
        }
        if (original.isMin()) {
            return "1000-01-03";
        }
        if (original.isMax()) {
            return "5000-01-01";
        }
        return original.toString();
    }

    /**
     * Wrap value from String to provys datatype when processing JSON data
     *
     * @param adapted is source value, read from JSON to String
     * @return wrapped provys datatype value
     */
    @Override
    public DtDate adaptFromJson(String adapted) {
        StringParser parser = new StringParser(adapted);
        try {
            var year = parser.readUnsignedInt(4);
            if (parser.next() != '-') {
                throw new DateTimeParseException("Expecting - as year / month delimiter", parser.getString(),
                        parser.getPos());
            }
            var month = parser.readUnsignedInt(2);
            if (parser.next() != '-') {
                throw new DateTimeParseException("Expecting - as year / month delimiter", parser.getString(),
                        parser.getPos());
            }
            var day = parser.readUnsignedInt(2);
            return DtDate.of(year, month, day, true);
        } catch (StringIndexOutOfBoundsException e) {
            throw new DateTimeParseException("Unexpected end of string encountered", parser.getString(),
                    parser.getPos());
        }
    }

}