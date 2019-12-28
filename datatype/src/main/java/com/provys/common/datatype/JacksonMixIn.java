package com.provys.common.datatype;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Class defines mapping of Dt classes to their MixIns for proper Jackson serialisation / deserialization.
 * Annotations are not used directly on classes as they would interfere with JAXB annotations
 */
@SuppressWarnings("WeakerAccess")
public class JacksonMixIn extends SimpleModule {
    public JacksonMixIn() {
        super("ProvysDtMixIn", new Version(1, 0, 0, null, null, null));
        setMixInAnnotation(DtDate.class, DtDateMixIn.class);
        setMixInAnnotation(DtTimeS.class, DtTimeSMixIn.class);
        setMixInAnnotation(DtUid.class, DtUidMixIn.class);
    }
}
