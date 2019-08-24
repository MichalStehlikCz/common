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
        return original.toIso();
    }

    /**
     * Wrap value from String to provys datatype when processing JSON data
     *
     * @param adapted is source value, read from JSON to String
     * @return wrapped provys datatype value
     */
    @Override
    public DtDate adaptFromJson(String adapted) {
        return DtDate.parseIso(adapted);
    }

}