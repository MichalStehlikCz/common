package com.provys.common.datatype;

import javax.json.bind.adapter.JsonbAdapter;

/**
 * Adapter ensuring provys time value is converted using standard ISO formatting
 * serializing to / deserializing from Json using JSON-B
 *
 * @author stehlik
 */
@SuppressWarnings("WeakerAccess")
public class JsonbDtTimeSAdapter implements JsonbAdapter<DtTimeS, String> {

    /**
     * Unwrap value to ensure it is properly serialized to Json
     *
     * @param original is source provys datatype value
     * @return unwrapped value (String)
     */
    @Override
    public String adaptToJson(DtTimeS original) {
        return original.toIso();
    }

    /**
     * Wrap value from String to provys datatype when processing JSON data
     *
     * @param adapted is source value, read from JSON to String
     * @return wrapped provys datatype value
     */
    @Override
    public DtTimeS adaptFromJson(String adapted) {
        return DtTimeS.parseIso(adapted);
    }
}
