package com.provys.common.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class contains Jackson annotation for DtTimeS; this way, we can have DtTimeS annotated for proper use of JSON-B and
 * JAXB and ignore JAXB adapter when using Jackson
 */
@JsonSerialize(using = DtTimeSSerializer.class)
@JsonDeserialize(using = DtTimeSDeserializer.class)
interface DtTimeSMixIn {}
