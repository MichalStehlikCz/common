package com.provys.common.datatype;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Class defines mapping of Dt classes to their MixIns for proper Jackson serialisation /
 * deserialization. Annotations are not used directly on classes as they would interfere with JAXB
 * annotations
 */
public class JacksonMixIn extends SimpleModule {

  @SuppressWarnings("nullness")
  public JacksonMixIn() {
    super("ProvysDtMixIn");
    setMixInAnnotation(DtDate.class, DtDateMixIn.class);
    setMixInAnnotation(DtTimeS.class, DtTimeSMixIn.class);
    setMixInAnnotation(DtUid.class, DtUidMixIn.class);
  }
}
