package com.provys.common.datatype;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonModule extends SimpleModule {

  private static final long serialVersionUID = 4641128591041071916L;

  /**
   * Constructor for JacksonMixIn class - activates mix-in annotation interfaces.
   */
  @SuppressWarnings("nullness")
  public JacksonModule() {
    super("ProvysCommonDatatypeModule");
    addSerializer(new DtDateSerializer());
    addDeserializer(DtDate.class, new DtDateDeserializer());
    addSerializer(new DtDateTimeSerializer());
    addDeserializer(DtDateTime.class, new DtDateTimeDeserializer());
    addSerializer(new DtTimeSSerializer());
    addDeserializer(DtTimeS.class, new DtTimeSDeserializer());
    addSerializer(new DtUidSerializer());
    addDeserializer(DtUid.class, new DtUidDeserializer());
  }
}
