package com.provys.common.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class contains Jackson annotation for DtUid; this way, we can have DtUid annotated for proper use of JSON-B and
 * JAXB and ignore JAXB adapter when using Jackson
 */
@JsonSerialize(using = DtUidSerializer.class)
@JsonDeserialize(using = DtUidDeserializer.class)
interface DtUidMixIn {
}
