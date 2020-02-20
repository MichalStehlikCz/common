package com.provys.common.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * Customize Jackson Json mapper for use in Spring
 */
@Component
@Order(-5)
class ProvysJacksonObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

  private static final Logger LOG = LogManager
      .getLogger(ProvysJacksonObjectMapperBuilderCustomizer.class);

  @Override
  public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
    LOG.debug("Use PROVYS Object Mapper customizer");
    jacksonObjectMapperBuilder.annotationIntrospector(
        AnnotationIntrospector.pair(new JacksonAnnotationIntrospector(),
            new JaxbAnnotationIntrospector(TypeFactory.defaultInstance())));
    jacksonObjectMapperBuilder.modules(new Jdk8Module(), new JavaTimeModule());
    jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
  }
}
