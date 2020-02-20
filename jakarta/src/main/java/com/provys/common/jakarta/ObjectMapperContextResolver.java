package com.provys.common.jakarta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.provys.common.jackson.JacksonMappers;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Configuration of Jackson Json mapper for Jax-Rs with Provys formatting rules
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return JacksonMappers.getJsonMapper();
  }

}