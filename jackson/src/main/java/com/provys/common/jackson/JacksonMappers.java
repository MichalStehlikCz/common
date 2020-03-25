package com.provys.common.jackson;

import com.ctc.wstx.api.WstxOutputProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Jackson mappers (Json, Xml) that should be used in standard Provys libraries. At the moment it is
 * believed that mappers can be static as Jackson project hopefully solved problems with contention
 * when using mappers simultaneously from multiple threads. It should be noted that these mappers
 * should NEVER be modified, as it might affect components that expect behaviour defined here.
 *
 * <p>If some modification of mapper is required (for example project needs different date format,
 * timezone etc.), such mapper should be constructed outside this class but can be customised using
 * supplied methods.
 */
public final class JacksonMappers {

  private static final ObjectMapper JSON_MAPPER;

  static {
    JSON_MAPPER = new ObjectMapper();
    JSON_MAPPER.setAnnotationIntrospector(
        AnnotationIntrospector.pair(new JacksonXmlAnnotationIntrospector(),
            new JacksonAnnotationIntrospector()))
        .findAndRegisterModules()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
  }

  public static ObjectMapper getJsonMapper() {
    return JSON_MAPPER;
  }

  private static final XmlMapper XML_MAPPER;

  static {
    XML_MAPPER = new XmlMapper();
    XML_MAPPER.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
    XML_MAPPER.getFactory()
        .getXMLOutputFactory()
        .setProperty(WstxOutputProperties.P_USE_DOUBLE_QUOTES_IN_XML_DECL, true);
    XML_MAPPER.setAnnotationIntrospector(
        AnnotationIntrospector.pair(new JacksonXmlAnnotationIntrospector(),
            new JacksonAnnotationIntrospector()))
        .findAndRegisterModules();
  }

  public static XmlMapper getXmlMapper() {
    return XML_MAPPER;
  }

  private JacksonMappers() {
  }
}
