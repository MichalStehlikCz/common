package com.provys.common.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class XmlMapperContextResolver implements ContextResolver<XmlMapper> {

    @Override
    public XmlMapper getContext(Class<?> type) {
        return JacksonMappers.getXmlMapper();
    }

}
