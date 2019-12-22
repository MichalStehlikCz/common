package com.provys.common.jackson;

import com.ctc.wstx.api.WstxOutputProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import javax.annotation.Nonnull;

public class JacksonMappers {

    @Nonnull
    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.setAnnotationIntrospector(
                AnnotationIntrospector.pair(new JacksonAnnotationIntrospector(),
                        new JaxbAnnotationIntrospector(TypeFactory.defaultInstance())))
                .registerModules(new Jdk8Module(), new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Nonnull
    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    @Nonnull
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        XML_MAPPER.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        XML_MAPPER.getFactory()
                .getXMLOutputFactory()
                .setProperty(WstxOutputProperties.P_USE_DOUBLE_QUOTES_IN_XML_DECL, true);
        XML_MAPPER.setAnnotationIntrospector(
                AnnotationIntrospector.pair(new JacksonAnnotationIntrospector(),
                        new JaxbAnnotationIntrospector(TypeFactory.defaultInstance())))
                .registerModules(new Jdk8Module(), new JavaTimeModule());
    }

    @Nonnull
    public static XmlMapper getXmlMapper() {
        return XML_MAPPER;
    }
}