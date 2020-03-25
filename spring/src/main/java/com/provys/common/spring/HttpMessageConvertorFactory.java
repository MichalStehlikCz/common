package com.provys.common.spring;

import com.ctc.wstx.api.WstxOutputProperties;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

/**
 * Configures Jackson Xml mapper for use in Spring.
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
class HttpMessageConvertorFactory {

  private static final Logger LOG = LogManager.getLogger(HttpMessageConvertorFactory.class);

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Bean
  public MappingJackson2XmlHttpMessageConverter provysJackson2XmlHttpMessageConverter(
      Jackson2ObjectMapperBuilder builder) {
    LOG.debug("Customize Jackson XML mapper");
    XmlMapper mapper = builder.annotationIntrospector(
        AnnotationIntrospector
            .pair(new JacksonXmlAnnotationIntrospector(), new JacksonAnnotationIntrospector()))
        .createXmlMapper(true)
        .build();
    mapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
    mapper.getFactory()
        .getXMLOutputFactory()
        .setProperty(WstxOutputProperties.P_USE_DOUBLE_QUOTES_IN_XML_DECL, true);
    return new MappingJackson2XmlHttpMessageConverter(mapper);
  }
}
