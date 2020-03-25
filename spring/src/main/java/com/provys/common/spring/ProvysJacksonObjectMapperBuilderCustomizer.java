package com.provys.common.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * Customizes Jackson Json mapper for use in Spring.
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
        AnnotationIntrospector.pair(new JacksonXmlAnnotationIntrospector(),
            new JacksonAnnotationIntrospector()));
    jacksonObjectMapperBuilder.findModulesViaServiceLoader(true);
    jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
  }
}
