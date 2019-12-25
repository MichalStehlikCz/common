package com.provys.common.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class contains Jackson annotation for DtDate; this way, we can have DtTimeS annotated for proper use of JSON-B and
 * JAXB and ignore JAXB adapter when using Jackson
 */
@JsonSerialize(using = DtDateSerializer.class)
@JsonDeserialize(using = DtDateDeserializer.class)
interface DtDateMixIn {}
