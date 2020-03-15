package com.provys.common.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class contains Jackson annotation for DtTimeS. This way, we can have DtTimeS annotated for proper
 * use of JSON-B and JAXB and ignore JAXB adapter when using Jackson
 */
@SuppressWarnings("MarkerInterface") // Used to apply Jackson annotations
@JsonSerialize(using = DtTimeSSerializer.class)
@JsonDeserialize(using = DtTimeSDeserializer.class)
interface DtTimeSMixIn {

}
